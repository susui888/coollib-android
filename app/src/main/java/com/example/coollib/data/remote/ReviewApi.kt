package com.example.coollib.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ReviewApi {

    @GET("reviews/{bookId}")
    suspend fun getReviewsByBook(@Path("bookId") bookId: Int): Response<List<ReviewDto>>

    @POST("reviews")
    suspend fun createReview(@Body review: ReviewDto): Response<ReviewDto>
}