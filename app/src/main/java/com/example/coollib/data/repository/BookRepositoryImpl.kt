package com.example.coollib.data.repository

import com.example.coollib.data.local.BookDao
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toEntity
import com.example.coollib.data.remote.BookApi
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Category
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.repository.BookRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class BookRepositoryImpl @Inject constructor(
        private val api: BookApi,
        private val bookDao: BookDao
    ) : BookRepository {

    override suspend fun searchBooks(query: SearchQuery): List<Book> =
        withContext(Dispatchers.IO) {
            val response = api.searchBooks(
                category = query.category,
                author = query.author,
                publisher = query.publisher,
                year = query.year,
                searchTerm = query.searchTerm
            )

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            response.body()?.map { it.toDomain() }.orEmpty()
    }


    override suspend fun getBookById(id: Int): Book? =
        withContext(Dispatchers.IO) {

            bookDao.getBookById(id)?.let { localBook ->
                val updated = localBook.copy(
                    updatedAt = System.currentTimeMillis()
                )
                bookDao.updateBook(updated)
                return@withContext localBook.toDomain()
            }

            val response = api.getBookById(id)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            response.body()?.also { dto ->
                bookDao.insertBook(dto.toEntity())
            }?.toDomain()
        }

    override suspend fun getCategory(): List<Category> =
        withContext(Dispatchers.IO) {
            val response = api.getCategory()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            response.body()?.map { it.toDomain() }.orEmpty()
        }

    override fun getBooks(limit: Int): Flow<List<Book>> =
        bookDao.getBooks(limit)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getNewestBooks(): List<Book> =
        withContext(Dispatchers.IO) {
            val response = api.getNewestBooks()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            response.body()?.map { it.toDomain() }.orEmpty()
        }

}
