package com.example.coollib.domain.repository

import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun allCartItems(): Flow<List<Cart>>

    fun getCartCount(): Flow<Int>

    fun isBookInCart(id: Int): Flow<Boolean>

    suspend fun removeFromCart(id: Int)

    suspend fun addToCart(book: Book)
}