package com.clay.weatherforecast

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Clayton Hatathlie on 10/18/21
 **/
object WeatherApi {
    val service: WeatherApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(authOkHttpClient())
            .baseUrl(BuildConfig.OPEN_WEATHER_BASE_URL)
            .build()
            .create(WeatherApiService::class.java)
    }
}

private val authOkHttpClient = {
    val httpClient = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
    }

    httpClient.addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()

            val apiKeyUrl = request.url.newBuilder()
                .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_KEY)
                .addQueryParameter("exclude", "hourly,minutely")
                .build()

            request = request.newBuilder()
                .url(apiKeyUrl)
                .build()

            return chain.proceed(request)
        }
    })

    httpClient.build()
}

interface WeatherApiService {

    @GET("onecall")
    suspend fun getCityWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ): Response<WeatherData>
}