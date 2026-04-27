package com.example.coollib.data.repository

import android.util.Log
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toDto
import com.example.coollib.data.remote.ReviewApi
import com.example.coollib.di.IoDispatcher
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.repository.ReviewRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val TAG = "ReviewRepository"

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ReviewRepository {

    override suspend fun getReviewsByBook(bookId: Int): List<Review> = withContext(ioDispatcher) {
        runCatching {
            val response = reviewApi.getReviewsByBook(bookId)

            if (!response.isSuccessful) {
                Log.e(TAG, "Failed to fetch reviews: ${response.code()}")
                return@withContext emptyList()
            }

            response.body()?.map { it.toDomain() } ?: emptyList()
        }.getOrElse { e ->
            Log.e(TAG, "Error fetching reviews for bookId: $bookId", e)
            emptyList()
        }
    }

    override suspend fun createReview(review: Review): Review? = withContext(ioDispatcher) {
        runCatching {
            val response = reviewApi.createReview(review.toDto())

            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Log.e(TAG, "Failed to create review: ${response.code()}")
                null
            }
        }.getOrElse { e ->
            Log.e(TAG, "Error creating review", e)
            null
        }
    }
}