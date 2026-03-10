package com.example.coollib.domain.model

data class Wishlist(
    val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val year: Int,
    val coverUrl: String = ""
)