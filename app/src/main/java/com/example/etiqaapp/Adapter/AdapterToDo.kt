package com.example.etiqaapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etiqaapp.*
import com.example.etiqaapp.Helper.Constants
import com.example.etiqaapp.Helper.MyDbHelper
import com.example.etiqaapp.Model.ModelToDo
import kotlinx.android.synthetic.main.todo_row.view.*
import java.text.SimpleDateFormat

class AdapterToDo() : RecyclerView.Adapter<AdapterToDo.HolderRecord>() {

    private var context: Context? = null
    private var toDoList: ArrayList<ModelToDo>? = null

    lateinit var dbHelper: MyDbHelper
    //orderby sql queries
    private val NEWEST_FIRST = "${Constants.C_DATE_START} DESC"

    constructor(context: Context?, toDoList: ArrayList<ModelToDo>?) : this() {
        this.context = context
        this.toDoList = toDoList


        dbHelper = MyDbHelper(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        //inflate layout
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.todo_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        //return item size
        return toDoList!!.size
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {

        //get data, set data
        val model = toDoList!![position]
        val id = model.id
        val title = model.title
        val dateStart = model.dateStart
        val dateEnd = model.dateEnd
        val timeLeft = model.timeLeft
        val status = model.status
        val dateUpdated = model.dateUpdated

        //set data to views
        holder.title.text = title
        holder.dateStart.text = displayDateOnly(dateStart)
        holder.dateEnd.text = displayDateOnly(dateEnd)
        holder.timeLeft.text = timeLeft
        if (status == 1) {
            holder.status.text = "Completed"
            holder.complete!!.isChecked = true
        } else {
            holder.status.text = "Incomplete"
            holder.complete!!.isChecked = false
        }

        //show record in new activity on clicking record
        holder.itemView.setOnClickListener {
            //parse id

            val intent = Intent(context, CreateToDoActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("TITLE", title)
            intent.putExtra("DATE_START", dateStart)
            intent.putExtra("DATE_END", dateEnd)
            intent.putExtra("TIME_LEFT", timeLeft)
            intent.putExtra("STATUS", status)
            intent.putExtra("DATE_UPDATED", dateUpdated)
            intent.putExtra("isEditMode", true)
            context!!.startActivity(intent)
        }

        holder.updateBtn.setOnClickListener {
            //show edit or delete
            showMoreOptions(
                position,
                id,
                title,
                dateStart,
                dateEnd,
                timeLeft,
                status,
                dateUpdated
            )
        }

        holder.complete?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dbHelper.updateToDoStatus(id, 1)
                holder.status.text = "Completed"
            } else {
                dbHelper.updateToDoStatus(id, 0)
                holder.status.text = "Incomplete"
            }
        }

    }

    private fun showMoreOptions(
        position: Int,
        id: String,
        title: String,
        dateStart: String,
        dateEnd: String,
        timeLeft: String,
        status: Int,
        dateUpdated: String
    ) {
        val options = arrayOf("Edit", "Delete")
        val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setItems(options) { dialog, which ->
            //handle item clicks
            if (which == 0) {
                //edit clicked
                val intent = Intent(context, CreateToDoActivity::class.java)
                intent.putExtra("ID", id)
                intent.putExtra("TITLE", title)
                intent.putExtra("DATE_START", dateStart)
                intent.putExtra("DATE_END", dateEnd)
                intent.putExtra("TIME_LEFT", timeLeft)
                intent.putExtra("STATUS", status)
                intent.putExtra("DATE_UPDATED", dateUpdated)
                intent.putExtra("isEditMode", true)
                context!!.startActivity(intent)
            } else {
                //delete clicked
                dbHelper.deleteRecord(id)
                //refresh page
                (context as MainActivity)!!.onResume()
            }
        }
        dialog.show()

    }

    inner class HolderRecord(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //views from todo_row.xml
        var title: TextView = itemView.textViewTitle
        var dateStart: TextView = itemView.textViewDateStartInfo
        var dateEnd: TextView = itemView.textViewDateEndInfo
        var timeLeft: TextView = itemView.textViewTimeLeftInfo
        var status: TextView = itemView.textViewStatusInfo
        var complete: CheckBox = itemView.checkBoxComplete
        var updateBtn: ImageButton = itemView.button_updateToDo
    }

    fun displayDateOnly(startDate: String): String {
        //To dsiplay date as shown in the example
        var start: String = startDate
        val index: Int = start.indexOf(" ")
        start = start.substring(0, index)
        var sdf = SimpleDateFormat("dd/MM/yyyy")
        var startDate = sdf.parse(start)
        var sdf2 = SimpleDateFormat("dd MMM yyyy")
        val date = sdf2.format(startDate)
        return date

    }

}