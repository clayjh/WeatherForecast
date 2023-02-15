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

    private var _lastLocation = MutableLiveData<Location>()
    val lastLocation: LiveData<Location> = _lastLocation

    private var _weatherResult = MutableLiveData<WeatherResult>()
    val weatherResult : LiveData<WeatherResult> = _weatherResult

    fun getCityWeather(location: Location) = viewModelScope.launch {
        val result = weatherRepo.getCityWeather(location)
        _weatherResult.value = when(result) {
            is WeatherResponse.OnSuccess ->
                WeatherResult.OnSuccess(result.weatherData)
            is WeatherResponse.OnError ->
                WeatherResult.OnError(result.error.localizedMessage?:"unknown")
        }
    }

    fun setLocation(location: Location) {
        _lastLocation.value = location
    }
}

sealed class WeatherResult {
    data class OnSuccess(val weatherData: WeatherData?) : WeatherResult()
    data class OnError(val error: String) : WeatherResult()
}