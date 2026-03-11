package com.example.coollib.domain.repository

import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.SearchQuery

interface BookRepository {

    suspend fun searchBooks(query: SearchQuery): List<Book>
    suspend fun getBookById(id: Int): Book?
}