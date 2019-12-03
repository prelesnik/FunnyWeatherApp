package edu.greenriver.it.funny_weather_app.controllers

import edu.greenriver.it.funny_weather_app.services.OpenWeatherService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api")
class FunnyWeatherApiController(val service: OpenWeatherService)
{
    @RequestMapping("/byzip")
    @ResponseBody
    fun funnyWeatherByZip(@RequestParam(name = "zip") zip: String): String
    {
        return service.getMessageByZip(zip)
    }
}