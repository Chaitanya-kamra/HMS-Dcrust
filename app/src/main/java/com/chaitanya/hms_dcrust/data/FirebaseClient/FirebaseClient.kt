package com.chaitanya.hms_dcrust.data.FirebaseClient

import android.util.Log
import com.chaitanya.hms_dcrust.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseClient @Inject constructor(private val database: FirebaseDatabase) {
    private val messagesRef = database.getReference("chat/messages")

    fun subscribeForNewMessage(updateListener: (list : ArrayList<Message>) -> Unit){
        try {
            messagesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Handle new messages
                    val messagesList = ArrayList<Message>()
                    for (messageSnapshot in snapshot.children) {
                        val message = messageSnapshot.getValue(Message::class.java)
                        message?.let { messagesList.add(it) }
                    }
                    println(messagesList)
                    updateListener(messagesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun sendMessage(message:Message, success:(Boolean) -> Unit){

        val newMessageRef = messagesRef.push()
        newMessageRef.setValue(message).addOnSuccessListener {
            success(true)
        }.addOnFailureListener {
            success(false)
        }
    }
}