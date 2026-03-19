package com.example.coollib.data.remote

import com.example.coollib.domain.model.LoanStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
open class LoanDto(
    @property:Json(name = "id")
    open val id: Int,

    @property:Json(name = "bookId")
    open val bookId: Int,

    @property:Json(name = "borrowDate")
    open val borrowDate: LocalDate,

    @property:Json(name = "dueDate")
    open val dueDate: LocalDate,

    @property:Json(name = "returnDate")
    open val returnDate: LocalDate? = null,

    @property:Json(name = "status")
    open val status: LoanStatus,
)