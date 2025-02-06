package com.example.prepay.response

data class SignupRequest(
    val email: String,
    val password: String,
    val nickname: String
) {
    constructor() : this("", "", "")
}
data class SignupResponse(
    val success: Boolean,
    val message: String
)