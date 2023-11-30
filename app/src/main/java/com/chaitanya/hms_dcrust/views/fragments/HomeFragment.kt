package com.chaitanya.hms_dcrust.views.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chaitanya.hms_dcrust.R
import com.chaitanya.hms_dcrust.databinding.FragmentHomeBinding
import com.chaitanya.hms_dcrust.model.WeatherResponse
import com.chaitanya.hms_dcrust.viewModel.HostelViewModel
import com.chaitanya.hms_dcrust.utils.DataHandler
import com.chaitanya.hms_dcrust.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel: HostelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authToken = TokenManager.getAuthToken(requireContext())
        if (authToken == null) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment2)
        }else{
            if (viewModel.userData == null){
                viewModel.setUser(authToken)
            }
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(Color.WHITE)

            // Observe the fetchLocation LiveData to detrmine whether to fetch the location data
            viewModel.fetchLocation.observe(viewLifecycleOwner) {
                if (it) {
                    initWeather()
                } else {
                    weatherLayout()
                    viewModel.weatherData.observe(viewLifecycleOwner) { data ->
                        setWeatherUI(data)
                    }
                }
            }
            binding.ivRefresh.setOnClickListener { initWeather() }
            binding.tryAgain.setOnClickListener { initWeather()}
        }



    }

    private fun setWeatherUI(data: WeatherResponse) {

        //Load and display weather icon using Glide

        Glide.with(this)
            .load("https:" + data.current.condition.icon)
            .error(R.drawable.baseline_broken_image_24)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivCondition)

        weatherLayout()

        binding.apply {
            tvCondition.text = data.current.condition.text
            tvName.text = data.location.name
            tvRegion.text = data.location.region
            tvCountry.text = data.location.country
            tvTemp.text = data.current.temp_c.toString()
            tvWind.text = data.current.wind_kph.toString()

            // Set the appropriate image based on whether it's day or night
            if (data.current.is_day == 0) {
                ivIsDay.setImageResource(R.drawable.moon_and_stars)
            } else {
                ivIsDay.setImageResource(R.drawable.sun_icon)
            }
        }
    }


    private fun initWeather() {
        try {
            // Observe the weather data using LiveData and handle different states
            viewModel.weatherDetails.observe(viewLifecycleOwner) { dataHandler ->
                when (dataHandler) {
                    is DataHandler.SUCCESS -> {
                        val data = dataHandler.data
                        if (data != null) {
                            viewModel.shouldFetch(false)
                        }
                    }

                    is DataHandler.ERROR -> {
                        error()
                        Log.e("Fa",dataHandler.message.toString())
                    }

                    is DataHandler.LOADING -> {
                        showProgressDialog("Getting Weather!")
                    }
                }
            }
        } catch (_: Exception) {
            error()
        }

        viewModel.getWeather(33.toString(), 34.toString())
    }

    private fun error() {
        binding.lnlProgress.visibility = View.INVISIBLE
        binding.Error.visibility = View.VISIBLE
        binding.lnlDetails.visibility = View.INVISIBLE
    }

    private fun weatherLayout() {
        binding.lnlProgress.visibility = View.INVISIBLE
        binding.lnlDetails.visibility = View.VISIBLE
        binding.Error.visibility = View.INVISIBLE
    }

    private fun showProgressDialog(message: String) {
        binding.tvProgress.text = message
        binding.lnlProgress.visibility = View.VISIBLE
        binding.Error.visibility = View.INVISIBLE
        binding.lnlDetails.visibility = View.INVISIBLE
    }
}