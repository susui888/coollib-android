package com.example.coollib.di

import android.content.Context
import androidx.room.Room
import com.example.coollib.data.local.AppDatabase
import com.example.coollib.data.local.BookDao
import com.example.coollib.data.local.CartDao
import com.example.coollib.data.local.SearchHistoryDao
import com.example.coollib.data.local.WishlistDao
import com.example.coollib.domain.model.Wishlist
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "coolLib_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideBookDao(
        database: AppDatabase
    ): BookDao = database.bookDao()

    @Provides
    fun provideCartDao(
        database: AppDatabase
    ): CartDao = database.cartDao()

    @Provides
    fun provideWishlistDao(
        database: AppDatabase
    ): WishlistDao = database.wishlistDao()

    @Provides
    fun provideSearchHistoryDao(
        database: AppDatabase
    ): SearchHistoryDao = database.searchHistoryDao()
}