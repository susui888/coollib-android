package com.example.coollib.data.mapper

import com.example.coollib.data.remote.ReviewDto
import com.example.coollib.domain.model.Review

fun ReviewDto.toDomain() = Review(
    id = this.id,
    bookId = this.bookId,
    userId = this.userId,
    rating = this.rating.toInt(),
    content = this.content ?: "",
    createdAt = this.createdAt
)

fun Review.toDto() = ReviewDto(
    id = this.id,
    bookId = this.bookId,
    userId = this.userId,
    rating = this.rating.toShort(),
    content = this.content.ifEmpty { null },
    createdAt = this.createdAt
)