package com.example.coollib.domain.usecase

import com.example.coollib.domain.model.Review
import com.example.coollib.domain.repository.ReviewRepository
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

}