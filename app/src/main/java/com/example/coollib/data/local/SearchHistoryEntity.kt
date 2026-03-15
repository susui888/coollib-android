package com.example.coollib.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity (
    @PrimaryKey
    val keyword: String,

    val createdAt: Long = System.currentTimeMillis(),
)