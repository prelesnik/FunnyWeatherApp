package edu.greenriver.it.funny_weather_app.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.greenriver.it.funny_weather_app.config.OpenWeatherConfig
import edu.greenriver.it.funny_weather_app.models.CurrentWeatherByZip
import edu.greenriver.it.funny_weather_app.models.FunnyWeatherResponse
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

@Service
class OpenWeatherService
{
    fun getWeatherByZip(zip: String): CurrentWeatherByZip
    {
        var url = URL("https://api.openweathermap.org/data/2.5/weather?zip=" + zip + "&appid=" + OpenWeatherConfig.apiKey)

        with(url.openConnection() as HttpURLConnection)
        {
            requestMethod = "GET"
            var jsonData = ""

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    jsonData += line
                }
            }

            //convert json string into an object
            val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshiBuilder.adapter(CurrentWeatherByZip::class.java)
            val weatherObject = jsonAdapter.fromJson(jsonData)

            if (weatherObject != null)
            {
                return weatherObject
            } else
            {
                throw IllegalArgumentException("Data was unable to be retrieved. " +
                        "Check to make sure the zipcode used is valid.")
            }
        }
    }

    fun getMessageByZip(zip: String): String
    {
        val service = OpenWeatherService()
        try
        {
            val weather = service.getWeatherByZip(zip)
            val tempInF = ((weather.main.temp - 273.15) * 9.0 / 5 + 32).roundToInt()
            val response = FunnyWeatherResponse(tempInF, getTempMessage(tempInF))

            val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshiBuilder.adapter(FunnyWeatherResponse::class.java)
            return jsonAdapter.toJson(response)

        } catch (e: FileNotFoundException)
        {
            return "400: Bad Request"
        }
    }

    fun getTempMessage(temp: Int): String
    {
        val message = when
        {
            temp < 10      -> "It's REALLY freaking cold!"
            temp in 10..32 -> "It's below freezing!"
            temp in 32..45 -> "It's getting pretty damn cold..."
            temp in 45..60 -> "It's pretty chilly!"
            temp in 60..75 -> "It's kinda nice!"
            temp in 75..85 -> "It's a nice day!"
            else           -> "It's fucking hot!"
        }
        return message
    }
}