package com.alura.sabia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.alura.sabia.ui.navigation.NavHost
import com.alura.sabia.ui.theme.SabiaTheme
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiKey = BuildConfig.apiKey
        println("apiKey: $apiKey")

        val model = GenerativeModel(
            "gemini-2.0-flash",
            apiKey
        )

        lifecycleScope.launch {
            val response = model.generateContent("Quando o Brasil foi descoberto?")
            println("response: ${response.text}")
        }

        setContent {
            SabiaTheme {
                NavHost(
                    rememberNavController()
                )
            }
        }
    }
}

