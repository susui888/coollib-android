package com.example.coollib.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookEntity(

    @PrimaryKey
    val id: Int,

    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String = "Publisher Unavailable",
    val year: Int = 0,
    val available: Boolean = true,
    val description: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)