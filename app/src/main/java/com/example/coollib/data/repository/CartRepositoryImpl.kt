package com.example.coollib.data.repository

import com.example.coollib.data.local.CartDao
import com.example.coollib.data.local.CartEntity
import com.example.coollib.data.mapper.toCartEntity
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.repository.CartRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun allCartItems(): Flow<List<Cart>> =
        cartDao.getAllCartItems()
            .map { list -> list.map { it.toDomain() } }

    override fun getCartCount(): Flow<Int> =
        cartDao.getCount()

    override fun isBookInCart(id: Int): Flow<Boolean> =
        cartDao.isBookInCart(id)

    override suspend fun removeFromCart(id: Int) =
        cartDao.removeFromCart(id)

    override suspend fun addToCart(book: Book) =
        cartDao.addToCart(book.toCartEntity())
}