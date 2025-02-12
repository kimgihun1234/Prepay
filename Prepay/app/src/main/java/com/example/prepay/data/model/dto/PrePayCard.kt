package com.example.prepay.data.model.dto

data class PrePayCard(
    val title: String,
    val subTitle: String,
    val balance: String,
    val color: Int // 카드 배경색
)