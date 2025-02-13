package com.alura.sabia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.alura.sabia.gemini.GeminiAPI
import com.alura.sabia.ui.navigation.NavHost
import com.alura.sabia.ui.theme.SabiaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val geminiAPI = GeminiAPI()

        lifecycleScope.launch {
           geminiAPI.generateContent("Quando o Android foi lançado?")
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

