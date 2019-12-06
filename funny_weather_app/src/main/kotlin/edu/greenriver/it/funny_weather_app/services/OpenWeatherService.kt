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

            if (jsonData.startsWith("{"))
            {
                //convert json string into an object
                val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshiBuilder.adapter(CurrentWeatherByZip::class.java)
                val weatherObject = jsonAdapter.fromJson(jsonData)
                if (weatherObject != null)
                {
                    return weatherObject
                }
            }
            throw IllegalArgumentException("Invalid Zip Code")
        }
    }

    fun getMessageByZip(zip: String): String
    {
        try
        {
            val weather = getWeatherByZip(zip)
            val tempInF = ((weather.main.temp - 273.15) * 9.0 / 5 + 32).roundToInt()
            val weatherIconId = weather.weather[0].icon
            val response = FunnyWeatherResponse(tempInF, getTempMessage(tempInF), weatherIconId)

            val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshiBuilder.adapter(FunnyWeatherResponse::class.java)
            return jsonAdapter.toJson(response)

        } catch (e: Exception)
        {
            return "400: Bad Request"
        }
    }

    fun getTempMessage(temp: Int): String
    {
        val message = when
        {
            temp < 20      -> "Below 20... F@#k!!"
            temp in 20..31 -> "Forget jock itch, I have freezer burn!"
            temp in 32..39 -> "Great.... not only do we have to smell your breath, now we have to see it too!"
            temp in 40..49 -> "My shrinkage has shrunk!"
            temp in 50..59 -> "Hope you took your depression meds!"
            temp in 60..68 -> "Who unplugged summer?"
            temp == 69 -> "Nice.... ;D"
            temp in 70..79 -> "Beware of shirtless fat guys!"
            temp in 80..90 -> "Why are you checking the weather?? GO OUTSIDE!"
            else           -> "It's 90's plus...?? I feel like a turkey on Thanksgiving!"
        }
        return message
    }
}