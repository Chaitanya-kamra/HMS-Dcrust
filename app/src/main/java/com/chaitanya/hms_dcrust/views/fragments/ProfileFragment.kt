package com.chaitanya.hms_dcrust.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.chaitanya.hms_dcrust.R
import com.chaitanya.hms_dcrust.databinding.FragmentHomeBinding
import com.chaitanya.hms_dcrust.databinding.FragmentProfileBinding
import com.chaitanya.hms_dcrust.utils.TokenManager
import com.chaitanya.hms_dcrust.viewModel.HostelViewModel
import com.chaitanya.hms_dcrust.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val viewModel: HostelViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentProfileBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authToken = TokenManager.getAuthToken(requireContext())
        if (viewModel.userData == null){
            if (authToken != null) {
                viewModel.setUser(authToken)
            }
        }
        binding.tvName.text = viewModel.userData?.name
        val userData = viewModel.userData

        val userDetailsText = """
    Roll No: ${userData?.rollNo ?: ""}
    
    Phone: ${userData?.phone ?: ""}
    
    Email: ${userData?.email ?: ""}
    
    Graduation Year: ${userData?.year?: ""}
""".trimIndent()

        binding.tvDetails.text = userDetailsText
        binding.ivLogout.setOnClickListener{
            TokenManager.clearAuthToken(requireContext())
            viewModel.clearAuthResponse()
            viewModel.userData = null
            (requireActivity() as MainActivity).finish()
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
    }
}