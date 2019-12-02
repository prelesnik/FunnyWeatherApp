package edu.greenriver.it.funny_weather_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FunnyWeatherAppApplication

fun main(args: Array<String>)
{
    runApplication<FunnyWeatherAppApplication>(*args)
}
