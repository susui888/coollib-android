package com.example.coollib.ui.mapper

import com.example.coollib.domain.model.Book
import com.example.coollib.ui.model.BookItemUiModel

fun Book.toUiModel() = BookItemUiModel(
    id = id,
    title = title,
    author = author,
    coverUrl = coverUrl
)