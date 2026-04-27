package com.example.coollib.domain.repository

import com.example.coollib.domain.model.Review

interface ReviewRepository {
    suspend fun getReviewsByBook(bookId: Int): List<Review>
    suspend fun createReview(review: Review): Review?
}