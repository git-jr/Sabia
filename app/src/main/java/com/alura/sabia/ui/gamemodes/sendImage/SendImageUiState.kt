package com.alura.sabia.ui.gamemodes.sendImage

import android.graphics.Bitmap

data class SendImageUiState(
    val load: Boolean = false,
    val subject: String = "",
    val language: String = "",
    val selectedImage: Bitmap? = null,
    val requestText: String = "",
    val showCamera: Boolean = false,
    val explanation: String = "",
    val showBottomSheetResult: Boolean = false
)