package com.alura.sabia.ui.gamemodes.selecttheme

import android.graphics.Bitmap

data class SelectThemeUiState(
    val themes: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val selectedTheme: String? = null,
    val selectedLanguage: String? = null,
    val selectedImage: Bitmap? = null,
    val load: Boolean = false,
    val showCamera: Boolean = false,
    val showBottomSheet: Boolean = false,
    val showThemeDialog: Boolean = false,
    val goToNextScreen: Boolean = false
)