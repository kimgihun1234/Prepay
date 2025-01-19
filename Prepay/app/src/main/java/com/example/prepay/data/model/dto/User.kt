package com.example.prepay

data class User(
    val id: String,
    val name: String,
    val pass: String,
    val stamps: Int
){
    constructor():this("", "","",0)
    constructor(id:String, pass:String):this(id, "",pass,0)
    constructor(id:String,name:String,pass:String):this(id,name,pass,0)
}