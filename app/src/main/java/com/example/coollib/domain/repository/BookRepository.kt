package com.example.coollib.domain.repository

import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Category
import com.example.coollib.domain.model.SearchQuery
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface BookRepository {

    suspend fun searchBooks(query: SearchQuery): List<Book>
    suspend fun getBookById(id: Int): Book?
    suspend fun getCategory(): List<Category>
    fun getBooks(limit: Int): Flow<List<Book>>

    suspend fun getNewestBooks(): List<Book>
}