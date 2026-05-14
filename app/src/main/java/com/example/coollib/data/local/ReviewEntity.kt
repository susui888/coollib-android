package com.example.coollib.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int,
    val userId: Int,
    val userName: String,
    val rating: Short,
    val content: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUrls: String = ""
)