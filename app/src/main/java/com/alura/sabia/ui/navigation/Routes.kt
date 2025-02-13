package com.alura.sabia.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object SelectTheme : Routes()

    @Serializable
    data object Complete : Routes()

    @Serializable
    data object SendImage : Routes()

    @Serializable
    data object EndGame : Routes()
}