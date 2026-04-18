package com.example.coollib.data.mapper

import com.example.coollib.data.local.BookEntity
import com.example.coollib.data.local.CartEntity
import com.example.coollib.data.local.WishlistEntity
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
    coverUrl = "${APIConfig.IMG_BOOK_COVER}/$isbn.webp",
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
    coverUrl = "${APIConfig.IMG_BOOK_COVER}/$isbn.webp",
)

fun Book.toCartEntity() = CartEntity(
    id = this.id,
    isbn = this.isbn,
    title = this.title,
    author = this.author,
    publisher = this.publisher,
    year = this.year,
    addedAt = System.currentTimeMillis()
)

fun Book.toWishlistEntity() = WishlistEntity(
    id = this.id,
    isbn = this.isbn,
    title = this.title,
    author = this.author,
    publisher = this.publisher,
    year = this.year,
    addedAt = System.currentTimeMillis()
)

fun Book.toEntity() = BookEntity(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    this.available,
    this.description
)

fun BookEntity.toDto() = BookDto(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    this.available,
    this.description,
)