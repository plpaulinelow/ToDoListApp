package com.example.etiqaapp.Helper

object Constants {
    //db name
    const val DB_NAME = "ETIQA_DB"
    //db version
    const val DB_VERSION = 1
    //table name
    const val TO_DO_TABLE = "TO_DO_TABLE"
    //col and fields of table
    const val C_ID = "ID"
    const val C_TITLE = "TITLE"
    const val C_DATE_START = "DATE_START"
    const val C_DATE_END = "DATE_END"
    const val C_TIME_LEFT = "TIME_LEFT"
    const val C_DATE_UPDATED = "DATE_UPDATED"
    const val C_STATUS = "STATUS"

    //create table
    const val CREATE_TABLE_TO_DO = (
            "CREATE TABLE " + TO_DO_TABLE + "("
                    + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_TITLE + " TEXT,"
                    + C_DATE_START + " TEXT,"
                    + C_DATE_END + " TEXT,"
                    + C_TIME_LEFT + " TEXT,"
                    + C_DATE_UPDATED + " TEXT,"
                    + C_STATUS + " INTEGER"
                    + ")"
            )
}