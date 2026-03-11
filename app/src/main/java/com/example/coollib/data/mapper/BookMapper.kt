package com.example.coollib.data.mapper

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


