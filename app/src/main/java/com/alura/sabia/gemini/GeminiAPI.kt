package com.alura.sabia.gemini

import android.util.Log
import com.alura.sabia.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig

class GeminiAPI(
    apiKey: String = BuildConfig.apiKey,
    modelName: String = "gemini-2.0-flash"
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

    suspend fun generateContent(prompt: String): String? {
        val response = model.generateContent(prompt)
        Log.d("GeminiAPI", "response: ${response.text}")
        return response.text
    }
}