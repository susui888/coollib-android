package com.example.coollib.data.mapper

import com.example.coollib.data.local.BookEntity
import com.example.coollib.data.remote.APIConfig
import com.example.coollib.data.remote.BookDto
import com.example.coollib.domain.model.Book


fun BookDto.toDomain() = Book(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    this.available,
    this.description,
    coverUrl = "${APIConfig.SERVER}/img/cover/$isbn.webp",
)

fun BookDto.toEntity() = BookEntity(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    this.available,
    this.description
)
fun BookEntity.toDomain() = Book(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    this.available,
    this.description,
    coverUrl = "${APIConfig.SERVER}/img/cover/$isbn.webp",
)
