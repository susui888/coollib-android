package com.example.coollib.domain.usecase


import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.repository.BookRepository
import javax.inject.Inject

class BookUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun searchBooks(query: SearchQuery): List<Book> {
        return repository.searchBooks(query).shuffled()
    }
}