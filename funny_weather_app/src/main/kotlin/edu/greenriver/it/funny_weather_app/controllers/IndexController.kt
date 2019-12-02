package edu.greenriver.it.funny_weather_app.controllers

import edu.greenriver.it.funny_weather_app.services.OpenWeatherService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.math.roundToInt

/**
 * This controller class provides the route for the index page
 * of the book lending application
 */
@Controller
class IndexController
{
    /**
     * The route to the index page of the application
     */
    @RequestMapping(path = ["", "/", "/index", "/index.html"])
    fun getIndexController(model: Model): String
    {
        val service = OpenWeatherService()
        model.addAttribute("weather", service.getWeatherByZip("98057"))
        return "index"
    }

    @RequestMapping(path = ["/{zip}"])
    fun getZipData(model: Model, @RequestParam(name = "zip") zip: String): String
    {
        val service = OpenWeatherService()
        val weather = service.getWeatherByZip(zip)
        val tempDouble = (weather.main.temp - 273.15) * 9.0/5 + 32
        val temp = tempDouble.roundToInt()

        model.addAttribute("temp", temp)
        return "index"
    }
}