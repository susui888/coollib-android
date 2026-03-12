package com.example.coollib.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    @property:Json(name = "id")
    val id: Int,

    @property:Json(name = "name")
    val name: String,

    @property:Json(name = "description")
    val description : String
)