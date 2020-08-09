package com.example.etiqaapp.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.etiqaapp.Model.ModelToDo

class MyDbHelper (context: Context?):SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Constants.CREATE_TABLE_TO_DO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TO_DO_TABLE)
        onCreate(db)
    }

    //insert record to db
    fun insertToDo(
        title:String?,
        dateStart:String?,
        dateEnd:String?,
        timeLeft:String?
    ):Long {
        val db = this.writableDatabase
        val values = ContentValues()
        //get current timestamp
        val timeStamp = System.currentTimeMillis()
        //insert
        values.put(Constants.C_TITLE, title)
        values.put(Constants.C_DATE_START, dateStart)
        values.put(Constants.C_DATE_END, dateEnd)
        //need to calculate time left
        values.put(Constants.C_TIME_LEFT, timeLeft)
        values.put(Constants.C_DATE_UPDATED, timeStamp)
        values.put(Constants.C_STATUS, 0)

        //return id when insert row
        val id = db.insert(Constants.TO_DO_TABLE, null, values)
        db.close()
        return id
    }

    //update record in db
    fun updateRecord(
        id:String,
        title:String?,
        dateStart:String?,
        dateEnd:String?,
        timeLeft:String?
    ):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        //get current timestamp
        val timeStamp = System.currentTimeMillis()
        //insert
        values.put(Constants.C_TITLE, title)
        values.put(Constants.C_DATE_START, dateStart)
        values.put(Constants.C_DATE_END, dateEnd)
        values.put(Constants.C_TIME_LEFT, timeLeft)
        values.put(Constants.C_DATE_UPDATED, timeStamp)

        return db.update(
            Constants.TO_DO_TABLE,
            values,
            "${Constants.C_ID}=?",
            arrayOf(id)).toLong()
    }

    //update status in db
    fun updateToDoStatus(
        id:String,
        status:Int
    ):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        //get current timestamp
        val timeStamp = System.currentTimeMillis()
        //insert
        values.put(Constants.C_STATUS, status)
        values.put(Constants.C_DATE_UPDATED, timeStamp)

        return db.update(
            Constants.TO_DO_TABLE,
            values,
            "${Constants.C_ID}=?",
            arrayOf(id)).toLong()
    }

    //get all data
    fun getAllToDo(orderBy:String):ArrayList<ModelToDo>{
        val toDoList = ArrayList<ModelToDo>()
        val selectQuery = "SELECT * FROM ${Constants.TO_DO_TABLE} ORDER BY $orderBy"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            do{
                val modelToDo = ModelToDo(
                    "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                    "" + cursor.getString(cursor.getColumnIndex(Constants.C_TITLE)),
                    "" + cursor.getString(cursor.getColumnIndex(Constants.C_DATE_START)),
                    "" + cursor.getString(cursor.getColumnIndex(Constants.C_DATE_END)),
                    "" + cursor.getString(cursor.getColumnIndex(Constants.C_TIME_LEFT)),
                    +cursor.getInt(cursor.getColumnIndex(Constants.C_STATUS)),
                    "" + cursor.getString(cursor.getColumnIndex(Constants.C_DATE_UPDATED))
                )
                //add recipe to list
                toDoList.add(modelToDo)
            }while (cursor.moveToNext())
        }
        db.close()
        return toDoList
    }


    //delete single record
    fun deleteRecord(id:String){
        val db = writableDatabase
        db.delete(
            Constants.TO_DO_TABLE,
            "${Constants.C_ID} = ?",
            arrayOf(id)
        )
        db.close()
    }

}