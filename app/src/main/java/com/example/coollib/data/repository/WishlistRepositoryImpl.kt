package com.example.coollib.data.repository

import com.example.coollib.data.local.WishlistDao
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toWishlistEntity
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Wishlist
import com.example.coollib.domain.repository.WishlistRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun allWishlist(): Flow<List<Wishlist>> =
        wishlistDao.getAllWishlist()
            .map { list -> list.map { it.toDomain() } }

    override fun getWishlistCount(): Flow<Int> =
        wishlistDao.getCount()

    override fun isBookInWishlist(id: Int): Flow<Boolean> =
        wishlistDao.isBookInWishlist(id)

    override suspend fun removeFromWishlist(id: Int) =
        wishlistDao.removeFromWishlist(id)

    override suspend fun addToWishlist(book: Book) =
        wishlistDao.addToWishlist(book.toWishlistEntity())
}