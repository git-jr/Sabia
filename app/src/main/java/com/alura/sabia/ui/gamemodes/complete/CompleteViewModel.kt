package com.alura.sabia.ui.gamemodes.complete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.sabia.gemini.Gemini
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
    private val gemini: Gemini,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompleteUiState())
    var uiState = _uiState.asStateFlow()

    fun generatePhrase(subject: String) {
        startLoad()
        val prompt = """
            Gere uma frase sobre o assunto: `$subject`, em inglês, com 2 a 4 palavras faltando. Sugira algumas palavras para preencher as lacunas e forneça as respostas corretas.

            Devolva o resultado no seguinte formato JSON:

            {
              "incomplete_sentence": "The world is ___ and ___",
              "correct_sentence": "The world is beautiful and diverse",
              "correct_answers": ["beautiful", "diverse"],
              "suggestions": ["amazing", "essential", "complex", "beautiful", "diverse"]
            }
        """.trimIndent()

        viewModelScope.launch {
            gemini.sendPrompt(
                prompt,
                onResponse = { response ->
                    println("response: $response")
                    processResponse(response)
                }
            )
        }
    }

    private fun startLoad() {
        _uiState.value = _uiState.value.copy(load = true)
    }


    fun processResponse(response: String) {
        val phraseToComplete = response.substringAfter("incomplete_sentence\": \"").substringBefore("\",")
        val completePhrase = response.substringAfter("correct_sentence\": \"").substringBefore("\",")
        val correctAnswers = response.substringAfter("correct_answers\": [\"").substringBefore("\"],").split("\", \"")
        val suggestions = response.substringAfter("suggestions\": [\"").substringBefore("\"]").split("\", \"")


        _uiState.value = _uiState.value.copy(
            load = false,
            phraseToComplete = phraseToComplete,
            completedPhrase = completePhrase,
            correctAnswers = correctAnswers,
            suggestions = suggestions
        )
    }

    fun checkAnswer(suggestion: String) {
        val currentPhrase = uiState.value.phraseToComplete
        if (!currentPhrase.contains("___")) return

        val newPhrase =currentPhrase.replaceFirst("___", "*${suggestion}*")
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
            isCorrect = isCorrect
        )
    }

}