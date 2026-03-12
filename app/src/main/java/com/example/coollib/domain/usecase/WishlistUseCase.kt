package com.example.coollib.domain.usecase

import com.example.coollib.domain.model.Wishlist
import com.example.coollib.domain.repository.BookRepository
import com.example.coollib.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val bookRepository: BookRepository
) {

    fun getWishlist(): Flow<List<Wishlist>> =
        wishlistRepository.allWishlist()

    fun getWishlistCount(): Flow<Int> =
        wishlistRepository.getWishlistCount()

    fun isBookInWishlist(bookId: Int): Flow<Boolean> =
        wishlistRepository.isBookInWishlist(bookId)

    suspend fun removeFromWishlist(bookId: Int) =
        wishlistRepository.removeFromWishlist(bookId)

    suspend fun addToWishlist(bookId: Int) {

        val book = bookRepository.getBookById(bookId) ?: return

        wishlistRepository.addToWishlist(book)
    }
}