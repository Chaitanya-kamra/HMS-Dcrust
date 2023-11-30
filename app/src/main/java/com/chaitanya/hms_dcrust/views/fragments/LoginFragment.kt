package com.chaitanya.hms_dcrust.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chaitanya.hms_dcrust.R
import com.chaitanya.hms_dcrust.databinding.FragmentChatBinding
import com.chaitanya.hms_dcrust.databinding.FragmentLoginBinding
import com.chaitanya.hms_dcrust.utils.DataHandler
import com.chaitanya.hms_dcrust.utils.TokenManager
import com.chaitanya.hms_dcrust.viewModel.HostelViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: HostelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogIn.setOnClickListener {
            if (binding.etEmail.text.isNullOrEmpty() && binding.etPassword.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Please enter required field",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }
        viewModel.authResponse.observe(viewLifecycleOwner){dataHandler->
            when (dataHandler) {
                is DataHandler.SUCCESS -> {
                    val data = dataHandler.data
                    if (data != null) {
                        Log.e("fa",data.toString())
                        TokenManager.saveAuthToken(requireContext(),data.data[0].rollNo)
                        findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)
                        binding.btnLogIn.isEnabled = true
                    }
                }

                is DataHandler.ERROR -> {
                    Toast.makeText(requireContext(),dataHandler.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("fa8",dataHandler.message.toString())
                    binding.btnLogIn.isEnabled = true
                }

                is DataHandler.LOADING -> {
                    binding.btnLogIn.isEnabled = false
                }

                else -> {}
            }

        }
    }

}