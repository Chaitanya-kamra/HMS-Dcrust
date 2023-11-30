package com.chaitanya.hms_dcrust.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.hms_dcrust.data.MyRepository
import com.chaitanya.hms_dcrust.model.Data
import com.chaitanya.hms_dcrust.model.LoginResponse
import com.chaitanya.hms_dcrust.model.Message
import com.chaitanya.hms_dcrust.model.WeatherResponse
import com.chaitanya.hms_dcrust.utils.Constants
import com.chaitanya.hms_dcrust.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HostelViewModel @Inject constructor(private val repository: MyRepository):ViewModel() {

    private val _authResponse = MutableLiveData<DataHandler<LoginResponse>?>()
    val authResponse : LiveData<DataHandler<LoginResponse>?> = _authResponse


    var userData : Data? = null

    fun setUser(username: String){
        viewModelScope.launch {
            val response = repository.getStudent(username)
            if (response.isSuccessful){
                response.body()?.let { it ->
                    userData = it.data[0]
                }
            }
        }
    }

    fun clearAuthResponse() {
        _authResponse.value = null
    }

    fun login(username :String,password:String){
        viewModelScope.launch{
            _authResponse.postValue(DataHandler.LOADING())
            try {
                val response =repository.login(username,password)
                _authResponse.postValue(handleLogInResponse(response))
            }catch (e:Exception){
                _authResponse.postValue(DataHandler.ERROR(message = e.message.toString()))
            }

        }
    }

    private fun handleLogInResponse(response: Response<LoginResponse>): DataHandler<LoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                userData = it.data[0]
                return DataHandler.SUCCESS(it)
            }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }


    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun getMessages() {
        _messages.postValue(messages.value)
    }

    fun loadMessages() {
        repository.subscribeForNewMessage {
            _messages.postValue(it)
        }
    }

    fun sendMessage(messageText: Message) {
        repository.sendMessage(message = messageText){

        }
    }



    private val _fetchLocation = MutableLiveData<Boolean>().apply { value = true }
    val fetchLocation: LiveData<Boolean> = _fetchLocation

    fun shouldFetch(shouldFetch :Boolean){
        _fetchLocation.value = shouldFetch
    }

    // Set weather data received from the network.
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData : LiveData<WeatherResponse> = _weatherData

    //Fetch weather details and use DataHandler to represent various States
    private val _weatherDetails = MutableLiveData<DataHandler<WeatherResponse>>()
    val weatherDetails : LiveData<DataHandler<WeatherResponse>> = _weatherDetails

    fun getWeather(latitude : String,longitude :String) {
        _weatherDetails.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            try {
                val response = repository.getWeather(latitude,longitude, Constants.API_KEY)
                _weatherDetails.postValue(handleResponse(response))
            }catch (e:Exception){
                _weatherDetails.postValue(DataHandler.ERROR(message = e.message.toString()))
            }

        }
    }

    private fun handleResponse(response: Response<WeatherResponse>): DataHandler<WeatherResponse> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                _weatherData.value = it
                return DataHandler.SUCCESS(it)
            }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }
}