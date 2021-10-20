package com.clay.weatherforecast

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedWeatherViewModel : ViewModel() {
    //TODO make injectable
    private val weatherRepo = WeatherRepository()

    private val weatherResult = MutableLiveData<WeatherResult>()
    fun liveWeatherResult() : LiveData<WeatherResult> = weatherResult

    fun getCityWeather(location: Location) = viewModelScope.launch(Dispatchers.Main) {
        val result = weatherRepo.getCityWeather(location)
        weatherResult.value = when(result) {
            is WeatherResponse.OnSuccess -> {
                WeatherResult.OnSuccess(result.weatherData)
            }
            is WeatherResponse.OnError -> WeatherResult.OnError(result.error.localizedMessage?:"unknown")
        }
    }
}

sealed class WeatherResult {
    data class OnSuccess(val weatherData: WeatherData?) : WeatherResult()
    data class OnError(val error: String) : WeatherResult()
}