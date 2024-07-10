package com.chat.ai.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chat.ai.R
import com.chat.ai.adapter.ChatAdapter
import com.chat.ai.model.Message
import com.chat.ai.utils.AlertDialogHelper
import com.chat.ai.utils.OpenAIHelper
import com.chat.ai.utils.ReplyCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class ChatActivity : AppCompatActivity(), ReplyCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: TextInputEditText
    private lateinit var sendButton: MaterialButton
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private var apiKey = ""

    private lateinit var openAiHelper: OpenAIHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)



        chatAdapter = ChatAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString()
            if (userMessage.isNotEmpty()) {
                sendMessage(userMessage)
                messageInput.text?.clear()
            }
        }

        openAiHelper = OpenAIHelper(this)
        fetchApiKey {
            if (it != null) {
                apiKey = it
                openAiHelper.setupApiClient(apiKey)
            }else{
                Toast.makeText(this,"failed to fetch api key",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun sendMessage(userMessage: String) {
        messages.add(Message(userMessage, true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)


        if (apiKey.isNotEmpty()) {
            openAiHelper.generateReply(userMessage, this)
        }else{
            Toast.makeText(this,"Api key is missing",Toast.LENGTH_SHORT).show()
        }


    }

    private fun fetchApiKey(onResult: (String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("api_key").document("open_ai")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val apiKey = document.getString("key")
                    onResult(apiKey)
                } else {
                    onResult(null)
                    Log.d("FetchApiKey", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                onResult(null)
                Log.d("FetchApiKey", "get failed with ", exception)
            }
    }

    override fun onReplyReceived(reply: String) {
        messages.add(Message(reply, false))
        chatAdapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }

    override fun onError(error: String) {
        AlertDialogHelper(this).showAlertDialog("Error", error)
    }
}
