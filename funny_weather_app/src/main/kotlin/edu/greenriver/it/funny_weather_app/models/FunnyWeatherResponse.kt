package edu.greenriver.it.funny_weather_app.models

data class FunnyWeatherResponse(
        val temp: Int,
        val message: String,
        val icon: String
)