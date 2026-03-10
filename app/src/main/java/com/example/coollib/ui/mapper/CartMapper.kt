package com.example.coollib.ui.mapper

import com.example.coollib.domain.model.Cart
import com.example.coollib.ui.model.BookItemUiModel

fun Cart.toUiModel() = BookItemUiModel(
    id = id,
    title = title,
    author = author,
    coverUrl = coverUrl
)