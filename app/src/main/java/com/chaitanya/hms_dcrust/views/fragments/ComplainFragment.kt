package com.chaitanya.hms_dcrust.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.chaitanya.hms_dcrust.R
import com.chaitanya.hms_dcrust.databinding.FragmentComplainBinding
import com.chaitanya.hms_dcrust.databinding.FragmentHomeBinding
import com.chaitanya.hms_dcrust.model.Message
import com.chaitanya.hms_dcrust.viewModel.HostelViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@AndroidEntryPoint
class ComplainFragment : Fragment() {

    private lateinit var binding: FragmentComplainBinding
    private val viewModel: HostelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentComplainBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.getReference("chat/messages")

        binding.ComplainFab.setOnClickListener {
            val message = Message("User1", "Hello, everyone!","Lost/Found", getFormattedTime(System.currentTimeMillis()))
            viewModel.sendMessage(message)
        }
        viewModel.loadMessages()
        viewModel.messages.observe(viewLifecycleOwner){
            println(it)
        }

    }
    private fun getFormattedTime(timestamp:Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val netDate = Date(timestamp)
        return sdf.format(netDate)
    }


}