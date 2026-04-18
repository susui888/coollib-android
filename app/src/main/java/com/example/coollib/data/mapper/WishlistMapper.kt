package com.example.coollib.data.mapper

import com.example.coollib.data.local.WishlistEntity
import com.example.coollib.data.remote.APIConfig
import com.example.coollib.domain.model.Wishlist

fun WishlistEntity.toDomain() = Wishlist(
    this.id,
    this.isbn,
    this.title,
    this.author,
    this.publisher,
    this.year,
    coverUrl = "${APIConfig.IMG_BOOK_COVER}/$isbn.webp",
)

