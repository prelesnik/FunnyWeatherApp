package edu.greenriver.it.funny_weather_app.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.greenriver.it.funny_weather_app.config.OpenWeatherConfig
import edu.greenriver.it.funny_weather_app.models.CurrentWeatherByZip
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.net.HttpURLConnection
import java.net.URL

@Service
class OpenWeatherService
{
    fun getWeatherByZip(zip: String): CurrentWeatherByZip
    {
        var url = URL("https://api.openweathermap.org/data/2.5/weather?zip=" + zip + "&appid=" + OpenWeatherConfig.apiKey)


        with(url.openConnection() as HttpURLConnection)
        {
            requestMethod = "GET"

            //println("\nSent 'GET' request to URL: $url; Response Code : $responseCode")

            var jsonData = ""

            inputStream.bufferedReader().use{
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
            }
            else
            {
                throw IllegalArgumentException("Data was unable to be retrieved. " +
                                    "Check to make sure the zipcode used is valid.")
            }
        }
    }
}