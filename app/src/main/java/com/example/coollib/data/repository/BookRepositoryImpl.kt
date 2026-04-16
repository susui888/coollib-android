package com.example.coollib.data.repository

import com.example.coollib.data.local.BookDao
import com.example.coollib.data.local.CategoryDao
import com.example.coollib.data.local.NewestBookRef
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class BookRepositoryImpl @Inject constructor(
        private val api: BookApi,
        private val bookDao: BookDao,
        private val categoryDao: CategoryDao,
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

    override suspend fun getBookByIsbn(isbn: String): Book? =
        withContext(Dispatchers.IO) {
            val response = api.getBookByIsbn(isbn)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            response.body()?.toDomain()
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
            val localData = categoryDao.getAllCategory().first()

            if (localData.isNotEmpty()) {
                return@withContext localData.map { it.toDomain() }
            }

            val response = api.getCategory()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            val remoteData = response.body().orEmpty()

            if (remoteData.isNotEmpty()) {
                val entities = remoteData.map { it.toEntity() }
                categoryDao.insertAll(entities)
            }

            remoteData.map { it.toDomain() }
        }

    override fun getBooks(limit: Int): Flow<List<Book>> =
        bookDao.getBooks(limit)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getNewestBooks(): List<Book> =
        withContext(Dispatchers.IO) {
            val cachedEntities = bookDao.getCachedNewestBooks().first()

            if (cachedEntities.isNotEmpty()) {
                return@withContext cachedEntities.map { it.toDomain() }
            }

            val response = api.getNewestBooks()

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            val networkBooks = response.body().orEmpty()
            val domainBooks = networkBooks.map { it.toDomain() }
            val bookEntities = domainBooks.map { it.toEntity() }

            val refs = domainBooks.mapIndexed { index, book ->
                NewestBookRef(
                    bookId = book.id,
                    priority = index
                )
            }

            bookDao.updateNewestCache(bookEntities, refs)

            return@withContext domainBooks
        }

}
