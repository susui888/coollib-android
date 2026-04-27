package com.example.coollib.data.repository

import com.example.coollib.data.local.CartDao
import com.example.coollib.data.mapper.toCartEntity
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toDto
import com.example.coollib.data.remote.CartApi
import com.example.coollib.di.IoDispatcher
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.repository.CartRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi,
    private val cartDao: CartDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CartRepository {

    override fun allCartItems(): Flow<List<Cart>> =
        cartDao.getAllCartItems()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(ioDispatcher)

    override fun getCartCount(): Flow<Int> =
        cartDao.getCount().flowOn(ioDispatcher)

    override fun isBookInCart(id: Int): Flow<Boolean> =
        cartDao.isBookInCart(id).flowOn(ioDispatcher)

    override suspend fun removeFromCart(id: Int) = withContext(ioDispatcher) {
        cartDao.removeFromCart(id)
    }

    override suspend fun addToCart(book: Book) = withContext(ioDispatcher) {
        cartDao.addToCart(book.toCartEntity())
    }

    override suspend fun borrowBooks(carts: List<Cart>): Result<String> = withContext(ioDispatcher) {
        try {
            val dtoList = carts.map { it.toDto() }
            val response = cartApi.borrowBooks(dtoList)
            val body = response.body()

            when {
                response.isSuccessful && body?.status == "success" -> {
                    clearLocalCart()
                    Result.success(body.message)
                }
                response.isSuccessful -> {
                    Result.failure(Exception(body?.message ?: "Failed to borrow books"))
                }
                else -> {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception("Error ${response.code()}: $errorMsg"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun clearLocalCart() = withContext(ioDispatcher) {
        cartDao.deleteAll()
    }
}
