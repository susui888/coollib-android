package com.example.coollib.data.mapper

import com.example.coollib.data.local.CartEntity
import com.example.coollib.data.remote.APIConfig
import com.example.coollib.data.remote.CartDto
import com.example.coollib.domain.model.Cart

fun CartEntity.toDomain() = Cart(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    coverUrl = "${APIConfig.IMG_BOOK_COVER}/$isbn.webp",
)
fun Cart.toDto() = CartDto(
    bookId = this.id,
    quantity = 1
)
