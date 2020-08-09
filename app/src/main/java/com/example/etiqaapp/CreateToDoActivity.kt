package com.example.etiqaapp

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_create_to_do.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.etiqaapp.Helper.MyDbHelper


class CreateToDoActivity : AppCompatActivity() {

    //action bar
    private var actionBar: ActionBar? = null

    //var save in database
    private var id: String = ""
    private var title: String? = ""
    private var dateStart: String? = ""
    private var dateEnd: String? = ""
    private var timeLeft: String? = ""
    private var status: Int = 0
    private var dateUpdated: String? = ""

    private var isEditMode = false

    lateinit var dbHelper: MyDbHelper

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_to_do)

        //init dbhelper
        dbHelper = MyDbHelper(this)

        //setting up actionbar
        actionBar = supportActionBar
        actionBar!!.title = "Add new To-Do List"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        editTextStartDate!!.showSoftInputOnFocus = false
        editTextStartDate.setOnClickListener {
            //date picker
            var c: Calendar = Calendar.getInstance()
            var dt = ""
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                    dt = "$dd/$mm/$yy"
                    TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hh, mi ->
                        dt += " $hh:$mi"
                        editTextStartDate.setText(dt)
                    }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false).show()
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        editTextEndDate!!.showSoftInputOnFocus = false
        editTextEndDate.setOnClickListener {
            //date picker
            var c: Calendar = Calendar.getInstance()
            var dt = ""
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                    dt = "$dd/$mm/$yy"
                    TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hh, mi ->
                        dt += " $hh:$mi"
                        if (checkDate(editTextStartDate.text.toString(), dt)) {
                            editTextEndDate.setText(dt)
                        } else {
                            dialogPopUp()
                        }
                    }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false).show()
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //create new button
        button_createToDo.setOnClickListener {
            if (inputData() > 0) {
                Toast.makeText(this, "To-Do task has been updated...", Toast.LENGTH_SHORT).show()
                finish();
            } else {
                Toast.makeText(this, "To-Do task update failed...", Toast.LENGTH_SHORT).show()
            }

        }

        //get data from intent
        val intent = intent
        isEditMode = intent.getBooleanExtra("isEditMode", false)
        if (isEditMode) {
            //edit data, values from adapter
            actionBar!!.title = "Update To-Do Task"
            button_createToDo.setText("Update To-Do Task")

            id = intent.getStringExtra("ID")
            title = intent.getStringExtra("TITLE")
            dateStart = intent.getStringExtra("DATE_START")
            dateEnd = intent.getStringExtra("DATE_END")
            timeLeft = intent.getStringExtra("TIME_LEFT")
            status = intent.getIntExtra("STATUS", 0)
            dateUpdated = intent.getStringExtra("DATE_UPDATED")

            editTextTitle.setText(title)
            editTextStartDate.setText(dateStart)
            editTextEndDate.setText(dateEnd)

        } else {
            //new data, values from MainActivity
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun inputData(): Long {
        title = editTextTitle.text.toString().trim()
        dateStart = editTextStartDate.text.toString().trim()
        dateEnd = editTextEndDate.text.toString().trim()
        timeLeft = calculateTimeLeft(dateStart.toString(), dateEnd.toString())


        if (isEditMode) {
            //editing
            val toDoId = dbHelper.updateRecord(
                "$id",
                "$title",
                "$dateStart",
                "$dateEnd",
                "$timeLeft"
            )
            return toDoId

        } else {
            //adding new
            //save data to db
            val insert = dbHelper.insertToDo(
                "" + title,
                "" + dateStart,
                "" + dateEnd,
                "" + timeLeft
            )
            return insert
        }
    }


    fun calculateTimeLeft(startDate: String, endDate: String): String {
        var start: String = startDate
        var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var startDate = sdf.parse(start)
        var end: String = endDate
        var endDate = sdf.parse(end)

        var days = (endDate.time - startDate.time) / 86400000
        var hours = (endDate.time - startDate.time) % 86400000 / 3600000
        var minutes = (endDate.time - startDate.time) % 86400000 % 3600000 / 60000
        var sec = (endDate.time - startDate.time) % 86400000 % 3600000 % 60000 / 1000
        var timeLeft = "" + days + " days " + hours + " hrs " + minutes + " min"
        return timeLeft
    }

    fun checkDate(startDate: String, endDate: String): Boolean {
        var start: String = startDate
        var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var startDate = sdf.parse(start)
        var end: String = endDate
        var endDate = sdf.parse(end)

        if (endDate.before(startDate) || startDate.equals("")) {
            return false
        }
        return true
    }

    fun dialogPopUp() {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle("Warning")
        // Display a message on alert dialog
        builder.setMessage("End Date cannot be before Start Date. Please select the date again..")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK") { dialog, which ->
            editTextEndDate.setText("")
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
