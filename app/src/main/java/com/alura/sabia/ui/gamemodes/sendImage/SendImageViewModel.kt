package com.alura.sabia.ui.gamemodes.sendImage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.sabia.dataStore.UserPreferencesDataStore
import com.alura.sabia.gemini.GeminiAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendImageViewModel @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
    private val geminiAPI: GeminiAPI
) : ViewModel() {
    private val _uiState = MutableStateFlow(SendImageUiState())
    var uiState = _uiState.asStateFlow()

    init {
        loadLanguage()
    }

    private fun loadLanguage() {
        startLoad()
        viewModelScope.launch {
            val language = dataStore.getSelectLanguage().first()
            val subject = dataStore.getSelectTheme().first()
            _uiState.value = _uiState.value.copy(language = language, subject = subject)

            requestSubjectImage()
        }
    }

    private fun startLoad() {
        _uiState.value = _uiState.value.copy(
            load = true
        )
    }

    private fun requestSubjectImage() {
        val subject = uiState.value.subject
        val language = uiState.value.language
        val prompt =
            """Dentro do tema '${subject}', sugira algo representativo relacionado a esse tema e
            peça para que eu envie uma foto. Responda apenas no seguinte formato:
            'Me envie uma imagem de [nome em '${language}']'.""".trimIndent()

        viewModelScope.launch {
            geminiAPI.useJsonFormat(false)
            geminiAPI.sendMessageChat(prompt)?.let { response ->
                _uiState.value = _uiState.value.copy(
                    requestText = response,
                    load = false
                )
            }
        }
    }

    fun requestAgain() {
        _uiState.value = _uiState.value.copy(
            load = true
        )
        requestSubjectImage()
    }

    fun checkImage() {
        viewModelScope.launch {
            _uiState.value.selectedImage?.let { selectedImage ->
                _uiState.value = _uiState.value.copy(
                    explanation = "",
                    load = true
                )

                val prompt = "Essa imagem mostra exatamente o que você pediu? Explique o porquê."
                val response = geminiAPI.generateContentWithImage(
                    prompt,
                    selectedImage,
                    useChat = true
                )

                response?.let {
                    _uiState.value = _uiState.value.copy(
                        load = false,
                        explanation = response,
                        showBottomSheetResult = true
                    )
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

    fun updateShowBottomSheetSuccess(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showBottomSheetResult = show
        )
    }
}