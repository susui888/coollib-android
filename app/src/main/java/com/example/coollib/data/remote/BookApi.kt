package com.example.coollib.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: Int): Response<BookDto>
    @GET("books/search")
    suspend fun searchBooks(
        @Query("category") category: Int? = null,
        @Query("author") author: String? = null,
        @Query("publisher") publisher: String? = null,
        @Query("year") year: Int? = null,
        @Query("searchTerm") searchTerm: String? = null
    ): Response<List<BookDto>>

    @GET("category")
    suspend fun getCategory(): Response<List<CategoryDto>>

    @GET("books/newest")
    suspend fun getNewestBooks(): Response<List<BookDto>>
}