package com.alura.sabia.model

import android.graphics.Bitmap

data class Message(
    val text: String? = null,
    val author: Author = Author.USER,
    val image: Bitmap? = null
)

enum class Author {
    USER,
    FUNCTION;

    fun displayName(): String {
        return name.lowercase()
    }
}