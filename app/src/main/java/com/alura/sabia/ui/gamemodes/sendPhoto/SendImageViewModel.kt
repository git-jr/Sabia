package com.alura.sabia.ui.gamemodes.sendPhoto

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.sabia.gemini.Gemini
import com.alura.sabia.model.Author
import com.alura.sabia.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun requestSubjectImage(
        subject: String
    ) {
        val prompt = "Dentro do tema '${subject}', sugira algo representativo relacionado a esse tema e peça para que eu envie uma foto. Responda apenas no seguinte formato: 'Me envie uma foto de [nome em inglês]'."
        viewModelScope.launch {
            gemini.sendPrompt(prompt,
                onResponse = { response ->
                    Log.d("SendImageViewModel", "SendImageViewModel response: $response")

                    val messagesList =  listOf(
                        Message(
                            text = prompt,
                            author = Author.USER
                        ),
                        Message(
                            text = response,
                            author = Author.MODEL
                        )
                    )

                    _uiState.value = _uiState.value.copy(
                        requestText = response,
                        load = false,
                        listMessage = _uiState.value.listMessage + messagesList
                    )
                }
            )
        }
    }

    fun checkImage() {
        val prompt = "Essa imagem responde a pergunta anterior? responda com 'sim' ou 'não'."


        viewModelScope.launch {
            _uiState.value.selectedImage?.let { selectedImage ->
                gemini.sendPromptWithImage(
                    prompt = prompt,
                    image = selectedImage,
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
                }
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