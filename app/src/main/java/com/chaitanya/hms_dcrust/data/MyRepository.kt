package com.chaitanya.hms_dcrust.data

import com.chaitanya.hms_dcrust.data.FirebaseClient.FirebaseClient
import com.chaitanya.hms_dcrust.data.api.HostelApi
import com.chaitanya.hms_dcrust.data.api.WeatherApiClient
import com.chaitanya.hms_dcrust.model.LoginResponse
import com.chaitanya.hms_dcrust.model.Message
import com.chaitanya.hms_dcrust.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

class MyRepository @Inject constructor(private val api : HostelApi ,private val firebaseClient: FirebaseClient) {

    suspend fun login(username :String,password:String):Response<LoginResponse>{
        return api.login(username,password)
    }
    suspend fun getStudent(
        username: String
    ): Response<LoginResponse>{
        return api.getStudent(username)
    }

    //Network
    suspend fun getWeather(latitude : String,longitude :String,apiKey : String) : Response<WeatherResponse> {
        val location = "$latitude,$longitude"
        return WeatherApiClient.newsApiService.getWeather(apiKey,location)
    }

    fun subscribeForNewMessage(updateListener: (list : ArrayList<Message>) -> Unit){
        firebaseClient.subscribeForNewMessage {
            updateListener(it)
        }
    }
    fun sendMessage(message:Message, success:(Boolean) -> Unit){
        firebaseClient.sendMessage(message = message){
            success(it)
        }
    }
}