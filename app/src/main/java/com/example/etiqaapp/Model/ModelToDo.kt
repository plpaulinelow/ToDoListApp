package com.example.etiqaapp.Model

class ModelToDo (
    var id:String,
    var title:String,
    var dateStart:String,
    var dateEnd:String,
    var timeLeft:String,
    var status:Int,
    var dateUpdated:String
)
{
    fun getID():String{
        return id
    }
}