package com.example.prepay.data.model.dto

data class PublicGroup (
    val public_team_name : String,
    val daily_price_limit : Int,
    val count_limit : String,
    val image_url : String,
    val input_text : String
)