package com.chaitanya.hms_dcrust.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.chaitanya.hms_dcrust.R
import com.chaitanya.hms_dcrust.databinding.FragmentChatBinding
import com.chaitanya.hms_dcrust.databinding.FragmentComplainBinding
import com.chaitanya.hms_dcrust.model.Message
import com.chaitanya.hms_dcrust.viewModel.HostelViewModel
import com.chaitanya.hms_dcrust.views.adapter.MessageAdapter
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.filterList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: HostelViewModel by activityViewModels()
    private lateinit var adapter: MessageAdapter
    private var type = "Normal"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentChatBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MessageAdapter(binding.rvMessage)
        binding.rvMessage.adapter = adapter
        viewModel.loadMessages()
        viewModel.messages.observe(viewLifecycleOwner){
                    if (binding.filterAll.isChecked){
                        adapter.submitList(it.reversed())
                        binding.rvMessage.scrollToPosition(0)
                    }else if (binding.filterImportant.isChecked){
                        adapter.submitList(it.filterList {
                            this.type == "Important"
                        }.reversed())
                        binding.rvMessage.scrollToPosition(0)
                    }else if (binding.filterLost.isChecked) {
                        adapter.submitList(it.filterList {
                            this.type == "Lost/Found"
                        }.reversed())
                        binding.rvMessage.scrollToPosition(0)
                    }else if (binding.filterNormal.isChecked){
                        adapter.submitList(it.filterList {
                            this.type == "Normal"
                        }.reversed())
                        binding.rvMessage.scrollToPosition(0)
                    }else{
                        binding.rvMessage.scrollToPosition(0)
                    }
        }
        binding.radioGroupFilter.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.getMessages()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.radioButtonNormal.id ->{
                    type = binding.radioButtonNormal.text.toString()
                }
                binding.radioButtonLostFound.id -> {
                    type = binding.radioButtonLostFound.text.toString()
                }
                binding.radioButtonImportant.id -> {
                    type = binding.radioButtonImportant.text.toString()
                }
            }
        }
        binding.btAdd.setOnClickListener {
            if (!binding.etMessage.text.isNullOrEmpty()) {
                val message = Message("User1", binding.etMessage.text.toString(),type, getFormattedTime(System.currentTimeMillis()))
                viewModel.sendMessage(message)
                binding.etMessage.text.clear()
                binding.rvMessage.scrollToPosition(0)
            } else {
                Toast.makeText(requireContext(), "Enter Message", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getFormattedTime(timestamp:Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val netDate = Date(timestamp)
        return sdf.format(netDate)
    }

}