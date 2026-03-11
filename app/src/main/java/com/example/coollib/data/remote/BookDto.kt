package com.example.coollib.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookDto(
    @property:Json(name = "id")
    val id: Int,

    @property:Json(name = "isbn")
    val isbn: String,

    @property:Json(name = "title")
    val title: String,

    @property:Json(name = "author")
    val author: String,

    @property:Json(name = "publisher")
    val publisher: String = "Publisher Unavailable",

    @property:Json(name = "year")
    val year: Int = 0,

    @property:Json(name = "available")
    val available: Boolean = true,

    @property:Json(name = "description")
    val description : String?
)