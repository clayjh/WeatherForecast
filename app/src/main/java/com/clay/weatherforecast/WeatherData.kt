package com.clay.weatherforecast

import java.util.*

/**
 * Created by Clayton Hatathlie on 10/18/21
 **/
data class WeatherData(
    val lat: Double,
    val lon: Double,
    val current: Current,
    val daily: List<Daily>
)

data class Current(
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDesc>
) {
    fun getLargeWeatherImage() : Int {
        return when(weather[0].main) {
            "Clouds", "Haze", "Smoke" -> R.drawable.clouds_large
            "Clear" -> R.drawable.sunny_large
            "Snow" -> R.drawable.snow_large
            "Rain", "Drizzle", "Thunderstorm" -> R.drawable.rain_large
            else -> R.drawable.sunny_large
            // TODO add more categories
        }
    }
}

data class Daily(
    val dt: Long,
    val temp: Temp,
    val weather: List<WeatherDesc>,
    val pop: Double // chance of rain
) {
    data class Temp(
        val min: Double,
        val max: Double
    )

    fun getRainPercentage() : Double = pop * 100F

    private fun getDate() : Date {
        return Date(dt * 1000)
    }

    fun getDay() : String {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.time = getDate()
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> ""
        }
    }
    fun getWeatherImage() : Int {
        return when(weather[0].main) {
            "Clouds", "Haze", "Smoke" -> R.drawable.clouds
            "Clear" -> R.drawable.sunny
            "Snow" -> R.drawable.snow
            "Rain", "Drizzle", "Thunderstorm" -> R.drawable.rain
            else -> R.drawable.sunny
            // TODO add more categories
        }
    }
}

data class WeatherDesc(
    val main: String
)

