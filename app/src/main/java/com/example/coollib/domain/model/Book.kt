package com.example.coollib.domain.model

data class Book(
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String = "Publisher Unavailable",
    val year: Int = 0,
    val available: Boolean = true,
    val description: String? = "",
    val coverUrl: String = "",
)