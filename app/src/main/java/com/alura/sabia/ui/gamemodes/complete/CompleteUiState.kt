package com.alura.sabia.ui.gamemodes.complete

data class CompleteUiState(
    val load: Boolean = false,
    val phraseToComplete: String = "",
    val completedPhrase: String = "",
    val correctAnswers: List<String> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val selectedSuggestions: List<String> = emptyList(),
    val isCorrect: Boolean? = null
)
