package com.alura.sabia.gemini

import android.graphics.Bitmap
import android.util.Log
import com.alura.sabia.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

class GeminiAPI(
    private val apiKey: String = BuildConfig.apiKey,
    private val modelName: String = "gemini-2.0-flash"
) {
    private var model: GenerativeModel

    init {
        model = GenerativeModel(
            modelName, apiKey,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            }
        )
    }

    fun useJsonFormat(yes: Boolean) {
        model = GenerativeModel(
            modelName, apiKey,
            generationConfig = generationConfig {
                responseMimeType = if (yes) "application/json" else "text/plain"
            }
        )
    }

    suspend fun generateContent(prompt: String): String? {
        val response = model.generateContent(prompt)
        Log.d("GeminiAPI", "response: ${response.text}")
        return response.text
    }

    suspend fun generateContentWithImage(
        prompt: String,
        image: Bitmap
    ): String? {
        val content = content {
            image(image)
            text(prompt)
        }

        val response = model.generateContent(content)
        Log.d("GeminiAPI", "response: ${response.text}")

        return response.text
    }
}