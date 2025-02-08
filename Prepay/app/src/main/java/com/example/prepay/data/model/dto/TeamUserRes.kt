package com.example.prepay

data class TeamUserRes(
    val id: String,
    val name: String,
    val pass: String
){
    constructor():this("", "","")
}