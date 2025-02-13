package com.alura.sabia.ui.gamemodes.complete

data class CompleteUiState(
    val load: Boolean = false,
    val subject: String = "",
    val language: String = "",
    val phraseToComplete: String = "",
    val completedPhrase: String = "",
    val translationPhrase: String = "",
    val correctAnswers: List<String> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val selectedSuggestions: List<String> = emptyList(),
    val isCorrect: Boolean? = null,
    val showBottomSheetError: Boolean = false,
    val showBottomSheetSuccess: Boolean = false
)
