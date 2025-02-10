package com.example.prepay.response

data class LoginRequest (
    val email : String,
    val password : String
){
    constructor() : this("", "")
}
//data class LoginResponse(
//    val nickname : String
//)