package com.example.coollib.data.repository

import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.remote.BookApi
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.repository.BookRepository
import jakarta.inject.Inject
import retrofit2.HttpException

class BookRepositoryImpl @Inject constructor(
        private val api: BookApi
    ) : BookRepository {

        override suspend fun searchBooks(query: SearchQuery): List<Book> {

            val response = api.searchBooks(
                category = query.category,
                author = query.author,
                publisher = query.publisher,
                year = query.year,
                searchTerm = query.searchTerm
            )

            if (!response.isSuccessful) {
                throw Exception("Http error ${response.code()}")
            }

            return response.body()?.map { it.toDomain() }.orEmpty()
        }
    }
