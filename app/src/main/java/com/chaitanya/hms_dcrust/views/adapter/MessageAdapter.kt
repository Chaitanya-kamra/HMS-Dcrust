package com.chaitanya.hms_dcrust.views.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chaitanya.hms_dcrust.databinding.ItemMessageBinding
import com.chaitanya.hms_dcrust.model.Message
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MessageAdapter(
    private val recyclerView: RecyclerView
) : ListAdapter<Message, MessageAdapter.ViewHolder>(DiffUtilCallBack()) {

    inner class ViewHolder(binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvMessage = binding.tvMessage
        val tvDate = binding.tvTime
        val typeBackGround = binding.mcvBack
        val tvType = binding.tvType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.apply {
            tvDate.text = message.timestamp
            tvMessage.text = message.sender +":- "+ message.message
            tvType.text = message.type
            typeBackGround.backgroundTintList = when(message.type){
                "Normal" -> ColorStateList.valueOf(Color.GREEN)
                "Lost/Found" -> ColorStateList.valueOf(Color.BLUE)
                "Important" -> ColorStateList.valueOf(Color.RED)
                else -> {ColorStateList.valueOf(Color.GREEN)}
            }
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Message>,
        currentList: MutableList<Message>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        recyclerView.smoothScrollToPosition(0)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.sender == newItem.sender && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }
}