package com.example.coollib.ui.previewSupport

import com.example.coollib.data.remote.APIConfig
import com.example.coollib.domain.model.Category

object MockCategory {
    val list = listOf(
        Category(
            id = 266,
            name = "Fiction",
            description = "No description",
            coverUrl = "${APIConfig.IMG_CATEGORY}/1.webp"
        ),
        Category(
            id = 267,
            name = "History",
            description = "No description",
            coverUrl = "${APIConfig.IMG_CATEGORY}/4.webp"
        ),
        Category(
            id = 266,
            name = "Science",
            description = "No description",
            coverUrl = "${APIConfig.IMG_CATEGORY}/2.webp"
        ),
        Category(
            id = 266,
            name = "Art",
            description = "No description",
            coverUrl = "${APIConfig.IMG_CATEGORY}/3.webp"
        )
    )
}