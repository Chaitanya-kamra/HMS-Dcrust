package com.chaitanya.hms_dcrust.data.api

import com.chaitanya.hms_dcrust.model.LoginResponse
import com.chaitanya.hms_dcrust.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HostelApi {

    @GET("verifyStudent/{username}/{password}")
    suspend fun login(
        @Path("username") username: String,
        @Path("password") password: String
    ): Response<LoginResponse>

    @GET("student/{username}")
    suspend fun getStudent(
        @Path("username") username: String
    ): Response<LoginResponse>


}