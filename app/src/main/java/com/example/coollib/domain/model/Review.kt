package com.example.coollib.domain.model

import java.time.Instant

data class Review(
    val id: Int?,
    val bookId: Int,
    val userId: Int,
    val rating: Int,
    val content: String,
    val createdAt: Instant
)