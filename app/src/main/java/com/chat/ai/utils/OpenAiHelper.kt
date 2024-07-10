package com.chat.ai.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Data classes for request and response
data class ChatRequest(val model: String, val messages: List<Message>)
data class Message(val role: String, val content: String)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)

interface OpenAIApi {
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun getChatCompletion(@Body request: ChatRequest): ChatResponse
}

// Define the interface for callback
interface ReplyCallback {
    fun onReplyReceived(reply: String)
    fun onError(error: String)
}

class OpenAIHelper(private val context: Context) {
    private lateinit var api: OpenAIApi
    private lateinit var apiKey: String

    fun setupApiClient(apiKey: String) {
        this.apiKey = apiKey
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val authInterceptor = Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val requestWithAuth: Request = originalRequest.newBuilder()
                .header("Authorization", "Bearer $apiKey")
                .build()
            chain.proceed(requestWithAuth)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(OpenAIApi::class.java)
    }

    fun generateReply(userMessage: String, callback: ReplyCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (!this@OpenAIHelper::apiKey.isInitialized) {
                    throw IllegalArgumentException("API key not initialized")
                }
                val messages = listOf(Message("user", userMessage))
                val request = ChatRequest(model = "gpt-3.5-turbo-0125", messages = messages)
                val response = api.getChatCompletion(request)
                val reply = response.choices.firstOrNull()?.message?.content ?: "No reply"

                CoroutineScope(Dispatchers.Main).launch {
                    callback.onReplyReceived(reply)
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    callback.onError("Error: ${e.message}")
                }
            }
        }
    }
}