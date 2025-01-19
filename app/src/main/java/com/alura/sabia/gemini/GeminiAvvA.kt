package com.alura.sabia.gemini

import android.graphics.Bitmap
import android.util.Log
import com.alura.sabia.model.Author
import com.alura.sabia.model.Message
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

class Gemini(private val apiKey: String) {

    private var modelName: String = "gemini-1.5-flash"

    private lateinit var generativeModel: GenerativeModel
    private lateinit var chat: Chat


    private fun loadModel() {
        generativeModel = GenerativeModel(
            modelName = this.modelName,
            apiKey = this.apiKey
        )
    }

    init {
        loadModel()
    }

    fun startChat(
        messages: List<Message> = emptyList()
    ) {
        val chatList: List<Content> = messages.map { message ->
            content(
                role = if (message.author == Author.USER) "user" else "model"
            ) {
                text(message.text)
                message.image?.let { image(it) }
            }
        }

        chat = generativeModel.startChat(chatList)
    }

    suspend fun sendPrompt(
        prompt: String,
        onResponse: (String) -> Unit = {}
    ) {
        val inputContent: Content = content {
            text(prompt)
        }

        try {
            generativeModel.generateContent(inputContent).let { response ->
                response.text?.let {
                    onResponse(it)
                }
            }
        } catch (e: Exception) {
            onResponse("Desculpe, houve um erro ao tentar processar a resposta.")
            e.printStackTrace()
        }
    }

    suspend fun sendChatPrompt(
        messages: Message,
        onResponse: (String) -> Unit = {}
    ) {

        val inputContent: Content = content(
            role = messages.author.name.lowercase()
        ) {
            text(messages.text)
            messages.image?.let { image(it) }
        }

        try {
            chat.sendMessage(inputContent).let { response ->
                response.text?.let {
                    onResponse(it)
                }
            }
        } catch (e: Exception) {
            onResponse("Desculpe, houve um erro ao tentar processar a resposta. $e")
            e.printStackTrace()
        }
    }

    suspend fun sendChatPromptStream(
        messages: Message,
        onResponse: (String) -> Unit = {}
    ) {
        val inputContent: Content = content(
            role = messages.author.name.lowercase()
        ) {
            text(messages.text)
            messages.image?.let { image(it) }
        }

        try {
            chat.sendMessageStream(inputContent).collect { response ->
                response.text?.let {
                    Log.d("SendImageViewModel", "SendImageViewModel generateExplain chat: ${response.text}")
                    delay(100)
                    onResponse(it)
                }
            }
        } catch (e: Exception) {
            onResponse("Desculpe, houve um erro ao tentar processar a resposta. $e")
            e.printStackTrace()
        }
    }

    suspend fun sendPromptWithImage(
        prompt: String,
        image: Bitmap,
        onResponse: (String) -> Unit = {}
    ) {
        val inputContent: Content = content {
            text(prompt)
            image(image)
        }

        try {
            generativeModel.generateContent(inputContent).let { response ->
                response.text?.let {
                    onResponse(it)
                }
            }
        } catch (e: Exception) {
            onResponse("Desculpe, houve um erro ao tentar processar a resposta.")
            e.printStackTrace()
        }
    }


    suspend fun sendPromptChat(
        messages: List<Message>,
        onResponse: (String) -> Unit = {}
    ) {
        val chatList: List<Content> = messages.map { message ->
            content(
                role = if (message.author == Author.USER) "user" else "model"
            ) {
                text(message.text)
                message.image?.let { image(it) }
            }
        }

        val chat = generativeModel.startChat(
            history = chatList
        )


        val inputContent: Content = content("AI") {
            messages.forEach {
                it.image?.let { image -> image(image) }
                text(it.text)
            }
        }

        try {
            chat.sendMessage(inputContent).let { response ->
                print(response.text)
                response.text?.let {
                    onResponse(it)
                }
            }
        } catch (e: Exception) {
            onResponse("Desculpe, houve um erro ao tentar processar a resposta.")
            e.printStackTrace()
        }
    }

    suspend fun sendPromptChatOld(
        prompt: String,
        imageList: List<Bitmap> = emptyList(),
        onResponse: (String) -> Unit = {}
    ) {

        val inputContent: Content = content {
            imageList.forEach {
                image(it)
            }
            text(prompt)
        }

        try {
            chat.sendMessage(inputContent).let { response ->
                print(response.text)
                response.text?.let {
                    onResponse(it)
                }
            }
        } catch (e: Exception) {
            onResponse("Desculpe, houve um erro ao tentar processar a resposta.")
            e.printStackTrace()
        }
    }


}