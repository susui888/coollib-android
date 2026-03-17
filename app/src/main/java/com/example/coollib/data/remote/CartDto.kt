package com.example.coollib.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CartDto (
    val bookId: Int,
    val quantity: Int = 1
)