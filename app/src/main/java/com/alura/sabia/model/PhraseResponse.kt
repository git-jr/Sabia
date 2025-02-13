package com.alura.sabia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhraseResponse(
    @SerialName("incomplete_sentence")
    val incompleteSentence: String,
    @SerialName("correct_sentence")
    val completePhrase: String,
    @SerialName("translation")
    val translationPhrase: String,
    @SerialName("correct_answers")
    val correctAnswers: List<String>,
    val suggestions: List<String>
)
