package com.example.coollib.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val id: Int,
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val year: Int,
    val addedAt: Long = System.currentTimeMillis()
)