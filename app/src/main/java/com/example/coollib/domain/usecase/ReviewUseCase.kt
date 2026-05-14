package com.example.coollib.domain.usecase

import android.net.Uri
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {

    suspend fun getReviewsByBook(bookId: Int): List<Review> {
        return repository.getReviewsByBook(bookId)
    }

    suspend fun createReview(review: Review): Review? {
        return repository.createReview(review)
    }

    suspend fun uploadImages(uris: List<Uri>): List<String> {
        return repository.uploadReviewImages(uris)
    }

    fun getAllLocalReviews(): Flow<List<Review>> {
        return repository.getAllLocalReviews()
    }

    suspend fun deleteReview(review: Review): Boolean {
        return repository.deleteReview(review)
    }
}