package edu.greenriver.it.funny_weather_app.controllers

import edu.greenriver.it.funny_weather_app.services.FunnyWeatherService
import edu.greenriver.it.funny_weather_app.services.OpenWeatherService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.FileNotFoundException
import kotlin.math.roundToInt

/**
 * This controller class provides the route for the index page
 * of the book lending application
 */
@Controller
class IndexController(val service: FunnyWeatherService)
{
    /**
     * The route to the index page of the application
     */
    @RequestMapping(path = ["", "/", "/index", "/index.html"])
    fun getIndexController(model: Model): String
    {
//        val service = OpenWeatherService()
//        model.addAttribute("weather", service.getWeatherByZip("98057"))
        model.addAttribute("firstVisit", true)
        return "index"
    }

    @RequestMapping(path = ["/zip"])
    fun getZipData(model: Model, @RequestParam(name = "zip") zip: String): String
    {
        val response = service.getWeatherResponse(zip)
        model.addAttribute("firstVisit", false)

        model.addAttribute("response", response)
        /*val service = OpenWeatherService()
        try
        {
            val weather = service.getWeatherByZip(zip)
            val tempDouble = (weather.main.temp - 273.15) * 9.0 / 5 + 32
            val response = tempDouble.roundToInt()
            model.addAttribute("response", "$response degrees fahrenheit")
        } catch (e: FileNotFoundException)
        {
            model.addAttribute("response", "Invalid Zip code")
        }*/
        return "index"
    }
}