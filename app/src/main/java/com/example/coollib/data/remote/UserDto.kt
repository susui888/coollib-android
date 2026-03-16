package com.example.coollib.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @property:Json(name = "username")
    val username: String,

    @property:Json(name = "password")
    val password: String,

    @property:Json(name = "email")
    val email : String
)