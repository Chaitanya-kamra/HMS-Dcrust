package com.chaitanya.hms_dcrust.data.api

import com.chaitanya.hms_dcrust.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v1/current.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): Response<WeatherResponse>
}