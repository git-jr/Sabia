package com.alura.sabia.gemini

import android.graphics.Bitmap
import com.alura.sabia.model.Author
import com.alura.sabia.model.Message
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.alura.sabia.apiKey as geminiApiKey

class Gemini {
    var defaultInstruction =
        "Você é um programa de inteligência artificial com foco em ajudar no aprendizado de idiomas."

    var apiKey: String = geminiApiKey
    private var modelName: String = "gemini-pro-vision"

    private lateinit var generativeModel: GenerativeModel
    private lateinit var chat: Chat

    fun setupModel(
        apiKey: String,
        modelName: String = "gemini-pro-vision"
    ) {
        this.apiKey = apiKey
        this.modelName = modelName

        loadModel()

    }

    private fun loadModel() {
        generativeModel = GenerativeModel(
            modelName = this.modelName,
            apiKey = this.apiKey
        )
        chat = generativeModel.startChat()
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
            text(defaultInstruction)
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


    fun redefineDefaultData(
        defaultInstruction: String? = "",
        apiKey: String? = ""
    ) {
        defaultInstruction?.let {
            this.defaultInstruction = it
        }

        apiKey?.let {
            this.apiKey = it
        }

        loadModel()
    }

}