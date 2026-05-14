package com.example.coollib.data.mapper

import com.example.coollib.data.local.ReviewEntity
import com.example.coollib.data.remote.ReviewDto
import com.example.coollib.domain.model.Review
import java.time.Instant

fun ReviewDto.toDomain() = Review(
    id = this.id,
    bookId = this.bookId,
    userId = this.userId,
    userName = this.userName,
    rating = this.rating.toInt(),
    content = this.content ?: "",
    createdAt = this.createdAt,
    imageUrls = this.imageUrls
)

fun Review.toDto() = ReviewDto(
    id = this.id,
    bookId = this.bookId,
    userId = this.userId,
    userName = this.userName,
    rating = this.rating.toShort(),
    content = this.content.ifEmpty { null },
    createdAt = this.createdAt,
    imageUrls = this.imageUrls
)

fun ReviewEntity.toDomain() = Review(
    id = this.id,
    bookId = this.bookId,
    userId = this.userId,
    userName = this.userName,
    rating = this.rating.toInt(),
    content = this.content ?: "",
    createdAt = Instant.ofEpochMilli(this.createdAt),
    imageUrls = if (this.imageUrls.isBlank()) emptyList() else this.imageUrls.split(",")
)

fun Review.toEntity() = ReviewEntity(
    id = this.id ?: 0,
    bookId = this.bookId,
    userId = this.userId,
    userName = this.userName,
    rating = this.rating.toShort(),
    content = this.content,
    createdAt = this.createdAt.toEpochMilli(),
    imageUrls = this.imageUrls.joinToString(",")
)

fun ReviewDto.toEntity() = ReviewEntity(
    id = this.id ?: 0,
    bookId = this.bookId,
    userId = this.userId,
    userName = this.userName,
    rating = this.rating,
    content = this.content,
    createdAt = this.createdAt.toEpochMilli(),
    imageUrls = this.imageUrls.joinToString(",")
)