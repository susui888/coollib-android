package com.example.coollib.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookEntity::class,
        CartEntity::class,
        WishlistEntity::class,
        SearchHistoryEntity::class,
        CategoryEntity::class,
        NewestBookRef::class,
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun categoryDao(): CategoryDao
}