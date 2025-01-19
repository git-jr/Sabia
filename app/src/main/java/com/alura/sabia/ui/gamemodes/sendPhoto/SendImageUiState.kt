package com.alura.sabia.ui.gamemodes.sendPhoto

import android.graphics.Bitmap
import com.alura.sabia.model.Message

data class SendImageUiState(
    val load: Boolean = false,
    val selectedImage: Bitmap? = null,
    val requestText: String = "",
    val isCorrect: Boolean? = null,
    val showCamera: Boolean = false,
    val listMessage: List<Message> = emptyList()
)