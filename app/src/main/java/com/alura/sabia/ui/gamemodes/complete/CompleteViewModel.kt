package com.alura.sabia.ui.gamemodes.complete

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
class CompleteViewModel @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {
    val geminiAPI = GeminiAPI()

    private val _uiState = MutableStateFlow(CompleteUiState())
    var uiState = _uiState.asStateFlow()

    init {
        loadLanguage()
    }

    private fun loadLanguage() {
        startLoad()
        viewModelScope.launch {
            val language = dataStore.getSelectLanguage().first()
            val subject = dataStore.getSelectTheme().first()
            _uiState.value =
                _uiState.value.copy(language = language, subject = subject, load = false)
            generatePhrase()
        }
    }

    private fun startLoad() {
        _uiState.value = _uiState.value.copy(load = true)
    }

    fun generatePhrase() {
        startLoad()
        val subject = uiState.value.subject
        val language = uiState.value.language

        val prompt =
            """Gere uma frase sobre o assunto: '$subject', em '$language', com 2 a 4 palavras faltando.
                Sugira algumas palavras para preencher as lacunas e forneça as respostas corretas.""".trimIndent()
        viewModelScope.launch {
            val response = geminiAPI.generateContent(prompt)

//        val phrase = """
//             {
//             "incomplete_sentence": "The sky is ___ and the trees are ___",
//             "correct_sentence": "The sky is blue and the trees are green",
//             "translation": "O céu é azul e as árvores são verdes",
//             "correct_answers": ["blue", "green"],
//             "suggestions": ["nuts", "blue", "oranges", "green", "red"]
//             }
//        """.trimIndent()

            processResponse(response.toString())
        }
    }

    private fun processResponse(response: String) {
        val phraseToComplete =
            response.substringAfter("incomplete_sentence\": \"").substringBefore("\",")
        val completePhrase =
            response.substringAfter("correct_sentence\": \"").substringBefore("\",")

        val translationPhrase =
            response.substringAfter("translation\": \"").substringBefore("\",")
        val correctAnswers =
            response.substringAfter("correct_answers\": [\"").substringBefore("\"],")
                .split("\", \"")
        val suggestions =
            response.substringAfter("suggestions\": [\"").substringBefore("\"]").split("\", \"")


        _uiState.value = _uiState.value.copy(
            load = false,
            phraseToComplete = phraseToComplete,
            completedPhrase = completePhrase,
            translationPhrase = translationPhrase,
            correctAnswers = correctAnswers,
            suggestions = suggestions
        )
    }

    fun checkAnswer(suggestion: String) {
        val currentPhrase = uiState.value.phraseToComplete
        val placeholderRegex = "_{3,5}".toRegex()

        if (!currentPhrase.contains(placeholderRegex)) return

        val newPhrase = currentPhrase.replaceFirst(placeholderRegex, "*${suggestion}*")

        _uiState.value = uiState.value.copy(
            selectedSuggestions = uiState.value.selectedSuggestions + suggestion,
            suggestions = uiState.value.suggestions - suggestion,
            phraseToComplete = newPhrase
        )
    }

    fun removeAnswer(suggestion: String) {
        val newPhrase = uiState.value.phraseToComplete.replaceFirst(suggestion, "___")
        _uiState.value = uiState.value.copy(
            selectedSuggestions = uiState.value.selectedSuggestions - suggestion,
            suggestions = uiState.value.suggestions + suggestion.replace("*", ""),
            phraseToComplete = newPhrase
        )
    }

    fun checkResult() {
        val userResponsePhrase = uiState.value.phraseToComplete.replace("*", "")
        val correctPhrase = uiState.value.completedPhrase

        val isCorrect = userResponsePhrase == correctPhrase
        _uiState.value = uiState.value.copy(
            isCorrect = isCorrect,
            showBottomSheetError = !isCorrect,
            showBottomSheetSuccess = isCorrect
        )
    }

    fun updateShowBottomSheetError(show: Boolean) {
        _uiState.value = uiState.value.copy(showBottomSheetError = show)
    }

    fun updateShowBottomSheetSuccess(show: Boolean) {
        _uiState.value = uiState.value.copy(showBottomSheetSuccess = show)
    }

}