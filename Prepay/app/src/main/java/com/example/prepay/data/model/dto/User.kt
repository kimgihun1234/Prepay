package com.example.prepay

data class User(
    val id: String,
    val name: String,
    val pass: String
){
    constructor():this("", "","")
}