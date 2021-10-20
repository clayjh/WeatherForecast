package com.clay.weatherforecast

import android.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Clayton Hatathlie on 10/18/21
 **/
class WeatherRepository {

    private val weatherApiService = WeatherApi.service //TODO inject

    /**
     * get city weather from lat, lon able to set fahrenheit or celsius
     *
     * TODO support celsius
     */
    suspend fun getCityWeather(
        location: Location,
        tempUnit: TempUnit = TempUnit.IMPERIAL
    ): WeatherResponse =
        withContext(Dispatchers.IO) {
            val result =
                weatherApiService.getCityWeather(
                    location.latitude,
                    location.longitude,
                    tempUnit.unit
                )
            if (result.isSuccessful) {
                WeatherResponse.OnSuccess(result.body())
            } else {
                WeatherResponse.OnError(Throwable("${result.code()}"))
            }
        }
}

sealed class WeatherResponse {
    data class OnSuccess(val weatherData: WeatherData?) : WeatherResponse()
    data class OnError(val error: Throwable) : WeatherResponse()
}