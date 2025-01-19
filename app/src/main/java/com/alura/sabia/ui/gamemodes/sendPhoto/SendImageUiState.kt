package com.alura.sabia.ui.gamemodes.sendPhoto

import android.graphics.Bitmap

data class SendImageUiState(
    val load: Boolean = false,
    val selectedImage: Bitmap? = null,
    val requestText: String = "",
    val isCorrect: Boolean? = null,
    val showCamera: Boolean = false,
    val explanation: String = ""
)