package com.alura.sabia.ui.gamemodes.sendPhoto

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.sabia.gemini.Gemini
import com.alura.sabia.model.Author
import com.alura.sabia.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendImageViewModel @Inject constructor(
    private val gemini: Gemini,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendImageUiState())
    var uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gemini.startChat()
        }
    }

    fun requestSubjectImage(
        subject: String
    ) {
        val prompt =
            "Dentro do tema '${subject}', sugira algo representativo relacionado a esse tema e peça para que eu envie uma foto. Responda apenas no seguinte formato: 'Me envie uma foto de [nome em inglês]'."
        val message = Message(
            text = prompt,
            author = Author.USER
        )
        viewModelScope.launch {
            gemini.sendChatPrompt(message,
                onResponse = { response ->
                    Log.d("SendImageViewModel", "SendImageViewModel response: $response")
                    _uiState.value = _uiState.value.copy(
                        requestText = response,
                        load = false
                    )
                }
            )
        }
    }

    fun checkImage() {
        _uiState.value = _uiState.value.copy(
            isCorrect = null,
            explanation = ""
        )
        viewModelScope.launch {
            _uiState.value.selectedImage?.let { selectedImage ->
                val prompt =
                    "Essa imagem responde a pergunta anterior? responda com 'sim' ou 'não'"
                val message = Message(
                    text = prompt,
                    author = Author.USER,
                    image = selectedImage
                )
                gemini.sendChatPrompt(
                    message
                ) { response ->
                    Log.d("SendImageViewModel", "SendImageViewModel response: $response")
                    if (response.contains("sim")) {
                        _uiState.value = _uiState.value.copy(
                            isCorrect = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isCorrect = false
                        )
                    }
                    generateExplain()
                }
            }
        }
    }

    private fun generateExplain() {
        viewModelScope.launch {
            val prompt = "Agora explique o motivo da sua resposta."
            val message = Message(
                text = prompt,
                author = Author.USER
            )
            gemini.sendChatPromptStream(
                message
            ) { response ->
                val explanation = _uiState.value.explanation + response
                Log.d("SendImageViewModel", "SendImageViewModel generateExplain: $response")
                _uiState.value = _uiState.value.copy(
                    explanation = explanation
                )
            }
        }
    }

    fun addNewItemImage(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(
            selectedImage = bitmap
        )
    }

    fun updateShowCameraState(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showCamera = show
        )
    }

    fun testImage() {
        viewModelScope.launch {
            gemini.sendPromptWithImage(
                prompt = "Descreva essa imagem",
                image = _uiState.value.selectedImage!!
            ) { response ->
                Log.d("SendImageViewModel", "SendImageViewModel response: $response")
            }
        }
    }
}