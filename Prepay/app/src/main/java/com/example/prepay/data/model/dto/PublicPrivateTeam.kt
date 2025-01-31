package com.example.prepay.data.model.dto

data class PublicPrivateTeam (
    val public_team : Int,
    val team_name : String,
    val daily_price_limit : Int,
    val count_limit : String,
    val team_message : String,
    val image_url : String,
) {
    constructor(
        public_team: Int,
        team_name: String,
        daily_price_limit: Int,
    ) : this(
        public_team,
        team_name,
        daily_price_limit,
        "",
        "",
        ""
    )
}

