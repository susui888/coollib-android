package com.example.coollib.domain.usecase


import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Category
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend fun searchBooks(query: SearchQuery): List<Book> =
        repository.searchBooks(query).shuffled()

    suspend fun getBookById(id: Int): Book? =
        repository.getBookById(id)


    suspend fun getCategory(): List<Category> =
        repository.getCategory()

    fun getBooks(limit: Int = 12): Flow<List<Book>> =
        repository.getBooks(limit)

    suspend fun getNewestBooks(): List<Book> =
       repository.getNewestBooks()
}