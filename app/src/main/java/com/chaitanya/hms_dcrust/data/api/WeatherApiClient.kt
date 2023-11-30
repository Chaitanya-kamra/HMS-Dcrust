package com.chaitanya.hms_dcrust.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiClient {
    private const val BASE_URL = "https://api.weatherapi.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val newsApiService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}