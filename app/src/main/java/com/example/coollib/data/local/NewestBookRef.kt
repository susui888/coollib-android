package com.example.coollib.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newest_book_refs")
data class NewestBookRef(
    @PrimaryKey val bookId: Int,
    val priority: Int
)