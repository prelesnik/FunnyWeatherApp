package edu.greenriver.it.funny_weather_app.models

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)