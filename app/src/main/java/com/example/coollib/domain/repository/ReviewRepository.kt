package com.example.coollib.domain.repository

import android.net.Uri
import com.example.coollib.domain.model.Review

interface ReviewRepository {
    suspend fun getReviewsByBook(bookId: Int): List<Review>
    suspend fun createReview(review: Review): Review?
    suspend fun uploadReviewImages(uris: List<Uri>): List<String>
}