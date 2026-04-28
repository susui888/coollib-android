package com.example.coollib.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
open class ReviewDto(
    @property:Json(name = "id")
    open val id: Int? = null,

    @property:Json(name = "bookId")
    open val bookId: Int,

    @property:Json(name = "userId")
    open val userId: Int,

    @property:Json(name = "userName")
    open val userName: String,

    @property:Json(name = "rating")
    open val rating: Short,

    @property:Json(name = "content")
    open val content: String? = null,

    @property:Json(name = "createdAt")
    open val createdAt: Instant
)