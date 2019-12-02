package edu.greenriver.it.funny_weather_app.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

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
    fun getIndexController(): String
    {
        return "index"
    }
}