package com.example.coollib.ui.mapper

import com.example.coollib.domain.model.Wishlist
import com.example.coollib.ui.model.BookItemUiModel

fun Wishlist.toUiModel() = BookItemUiModel(
    id = id,
    title = title,
    author = author,
    coverUrl = coverUrl
)