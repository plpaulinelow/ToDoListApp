package com.example.etiqaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.etiqaapp.Adapter.AdapterToDo
import com.example.etiqaapp.Helper.Constants
import com.example.etiqaapp.Helper.MyDbHelper
import com.example.etiqaapp.Model.ModelToDo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recycleView_main
//import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    //db helper
    lateinit var dbHelper: MyDbHelper
    private var toDoList: ArrayList<ModelToDo>? = null

    //orderby sql queries
    private val NEWEST_FIRST = "${Constants.C_DATE_START} DESC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //dbhelper
        dbHelper = MyDbHelper(this)

        loadRecords()

        button_addToDo.setOnClickListener {
            val intent = Intent(this, CreateToDoActivity::class.java)
            intent.putExtra("isEditMode", false)
            //new To-Do record, set it false
            startActivity(intent)
        }

    }

    private fun loadRecords() {
        //update the To-Do list to adapter
        toDoList = dbHelper.getAllToDo(NEWEST_FIRST)
        val adapterToDo = AdapterToDo(this, toDoList)
        recycleView_main.adapter = adapterToDo
    }



    public override fun onResume() {
        super.onResume()
        //refresh To-Do list
        loadRecords()
    }


}
