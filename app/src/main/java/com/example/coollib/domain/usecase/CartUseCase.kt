package com.example.coollib.domain.usecase

import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.repository.BookRepository
import com.example.coollib.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val bookRepository: BookRepository
) {

    fun getCartItems(): Flow<List<Cart>> =
        cartRepository.allCartItems()

    fun getCartCount(): Flow<Int> =
        cartRepository.getCartCount()

    fun isBookInCart(bookId: Int): Flow<Boolean> =
        cartRepository.isBookInCart(bookId)

    suspend fun removeFromCart(bookId: Int) =
        cartRepository.removeFromCart(bookId)

    suspend fun addToCart(bookId: Int) {
        val book = bookRepository.getBookById(bookId) ?: return
        cartRepository.addToCart(book)
    }

    suspend fun borrowBooks(carts: List<Cart>): Result<String> =
        cartRepository.borrowBooks(carts)
}
