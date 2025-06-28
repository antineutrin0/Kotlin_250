package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// Data classes for Gemini API request/response
data class GeminiRequest(val model: String, val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)
data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)
data class Message(val role: String, val content: String)

// Retrofit API interface
interface GeminiApiService {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    fun sendMessage(
        @Body request: GeminiRequest,
        @Query("key") apiKey: String
    ): Call<GeminiResponse>
}

class AiChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var userMessage: EditText
    private lateinit var sendButton: Button

    private val chatMessages = mutableListOf<Message>()
    private lateinit var adapter: ChatAdapter

    private val apiKey = "AIzaSyDJgVm6cXBSb0V3FQJXweFvxceQjmOY86g"  // Your actual API key

    private val api: GeminiApiService by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(GeminiApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_chat)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        userMessage = findViewById(R.id.userMessage)
        sendButton = findViewById(R.id.sendButton)

        adapter = ChatAdapter(chatMessages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        sendButton.setOnClickListener {
            val input = userMessage.text.toString().trim()
            if (input.isNotEmpty()) {
                addMessage(Message("user", input))
                userMessage.setText("")
                sendToGemini(input)
            }
        }
    }

    private fun addMessage(message: Message) {
        chatMessages.add(message)
        adapter.notifyItemInserted(chatMessages.size - 1)
        chatRecyclerView.scrollToPosition(chatMessages.size - 1)
    }

    private fun sendToGemini(prompt: String) {
        val request = GeminiRequest(
            model = "gemini-2.5-flash",
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            )
        )

        api.sendMessage(request, apiKey).enqueue(object : Callback<GeminiResponse> {
            override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
                if (response.isSuccessful) {
                    val reply = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: "No response from AI"
                    addMessage(Message("assistant", reply))
                } else {
                    addMessage(Message("assistant", "Error: ${response.code()} ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                addMessage(Message("assistant", "Error: ${t.localizedMessage}"))
            }
        })
    }

    inner class ChatAdapter(private val messages: List<Message>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return if (messages[position].role == "user") 0 else 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val layout = if (viewType == 0)
                R.layout.chat_item_user
            else
                R.layout.chat_item_ai
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return ChatViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            holder.bind(messages[position])
        }

        override fun getItemCount(): Int = messages.size

        inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val chatText: TextView = view.findViewById(R.id.chat_text)
            fun bind(message: Message) {
                chatText.text = message.content
            }
        }
    }
}
