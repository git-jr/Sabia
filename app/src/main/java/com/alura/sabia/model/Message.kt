package com.alura.sabia.model

data class Message(
    val text: String = "",
    val author: Author = Author.AI,
    val image: String = ""
)

enum class Author { USER, AI }
