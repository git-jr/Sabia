package com.alura.sabia.gemini

import android.graphics.Bitmap
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content

class Gemini(private val apiKey: String) {

    private var modelName: String = "gemini-1.5-flash"

    private lateinit var generativeModel: GenerativeModel
    private lateinit var chat: Chat


    private fun loadModel() {
        generativeModel = GenerativeModel(
            modelName = this.modelName,
            apiKey = this.apiKey
        )
        chat = generativeModel.startChat()
    }

    suspend fun sendPrompt(
        prompt: String,
        onResponse: (String) -> Unit = {}
    ) {
        this.modelName = "gemini-1.5-flash"
        loadModel()

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

    suspend fun sendPromptChat(
        prompt: String,
        imageList: List<Bitmap> = emptyList(),
        onResponse: (String) -> Unit = {}
    ) {
        if (imageList.isNotEmpty()) {
            this.modelName = "gemini-pro-vision"
            loadModel()
        } else if (this.modelName != "gemini-pro") {
            this.modelName = "gemini-pro"
            loadModel()
        }


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