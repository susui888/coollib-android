package com.example.coollib.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, formatter)
    }

    @ToJson
    fun toJson(value: LocalDate): String {
        return value.format(formatter)
    }
}