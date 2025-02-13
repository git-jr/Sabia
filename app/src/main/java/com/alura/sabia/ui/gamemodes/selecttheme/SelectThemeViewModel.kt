package com.alura.sabia.ui.gamemodes.selecttheme

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.sabia.data.ThemeRepository
import com.alura.sabia.dataStore.UserPreferencesDataStore
import com.alura.sabia.gemini.GeminiAPI
import com.alura.sabia.model.ThemeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SelectThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val dataStore: UserPreferencesDataStore,
    private val geminiAPI: GeminiAPI
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectThemeUiState())
    var uiState = _uiState.asStateFlow()

    init {
        getThemes()
        loadPreviousState()
    }

    private fun getThemes() {
        startLoad()

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                load = false,
                themes = themeRepository.getThemes(),
                languages = themeRepository.getLanguages()
            )
        }
    }

    private fun loadPreviousState() {
        viewModelScope.launch {
            val selectedTheme = dataStore.getSelectTheme().first()
            val selectedLanguage = dataStore.getSelectLanguage().first()
            _uiState.value = _uiState.value.copy(
                selectedTheme = selectedTheme,
                selectedLanguage = selectedLanguage
            )
        }
    }

    private fun startLoad() {
        _uiState.value = _uiState.value.copy(load = true)
    }

    fun generateThemByImage() {
        startLoad()

        viewModelScope.launch {
            val prompt = """
                Use essa imagem para gerar um tema de estudo de idiomas, 
                algo como 'Animais', 'Profissões', 'Comida', etc. 
                Retorne um json: "theme": "nome do tema em português". 
            """.trimIndent()

            _uiState.value.selectedImage?.let { image ->
                geminiAPI.generateContentWithImage(
                    image = image,
                    prompt = prompt
                )?.let { response ->
                    val theme = Json.decodeFromString<ThemeResponse>(response).theme
                    _uiState.value = _uiState.value.copy(
                        load = false,
                        selectedTheme = theme
                    )
                }
            }
        }
    }

    fun selectTheme(theme: String) {
        _uiState.value = _uiState.value.copy(selectedTheme = theme)
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

    fun updateShowBottomSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showBottomSheet = show
        )
    }

    fun removeSelectedImage() {
        _uiState.value = _uiState.value.copy(
            selectedImage = null,
            showBottomSheet = false
        )
    }

    fun start() {
        viewModelScope.launch {
            val selectedTheme = _uiState.value.selectedTheme
            val selectedLanguage = _uiState.value.selectedLanguage
            if (selectedTheme != null && selectedLanguage != null) {
                dataStore.saveSelectTheme(selectedTheme)
                dataStore.saveSelectLanguage(selectedLanguage)
            }
            _uiState.value = _uiState.value.copy(
                goToNextScreen = true
            )
        }
    }

    fun selectLanguage(language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }
}