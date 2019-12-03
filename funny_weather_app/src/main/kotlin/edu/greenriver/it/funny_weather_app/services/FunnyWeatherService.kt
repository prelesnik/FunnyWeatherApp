package edu.greenriver.it.funny_weather_app.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.greenriver.it.funny_weather_app.models.FunnyWeatherResponse
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL

@Service
class FunnyWeatherService
{
    fun getWeatherResponse(zip: String): FunnyWeatherResponse
    {
        var url = URL("http://localhost:8080/api/byzip?zip=$zip")

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
                val jsonAdapter = moshiBuilder.adapter(FunnyWeatherResponse::class.java)
                val response = jsonAdapter.fromJson(jsonData)
                if (response != null)
                {
                    return response
                }
            }
            return FunnyWeatherResponse(-460, "I don't know what planet you're from, but it sure ain't here!")
        }
    }
}