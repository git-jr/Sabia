package com.alura.sabia.model

import android.graphics.Bitmap

data class Message(
    val text: String = "",
    val author: Author = Author.MODEL,
    val image: Bitmap? = null
)

enum class Author { USER, MODEL }
