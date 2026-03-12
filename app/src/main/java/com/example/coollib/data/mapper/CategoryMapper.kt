package com.example.coollib.data.mapper

import com.example.coollib.data.remote.APIConfig
import com.example.coollib.data.remote.CategoryDto
import com.example.coollib.domain.model.Category


fun CategoryDto.toDomain() = Category(
    this.id,
    this.name,
    this.description,
    coverUrl = "${APIConfig.SERVER}/img/$id.webp",
)

