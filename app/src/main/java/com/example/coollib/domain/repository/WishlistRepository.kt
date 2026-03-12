package com.example.coollib.domain.repository

import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Wishlist
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {

    fun allWishlist(): Flow<List<Wishlist>>

    fun getWishlistCount(): Flow<Int>

    fun isBookInWishlist(id: Int): Flow<Boolean>

    suspend fun removeFromWishlist(id: Int)

    suspend fun addToWishlist(book: Book)
}