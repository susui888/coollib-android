package com.example.coollib.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import com.example.coollib.data.local.BookDao
import com.example.coollib.data.local.ReviewDao
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toDto
import com.example.coollib.data.mapper.toEntity
import com.example.coollib.data.remote.APIConfig
import com.example.coollib.data.remote.ReviewApi
import com.example.coollib.di.IoDispatcher
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.repository.ReviewRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

private const val TAG = "ReviewRepository"

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    private val reviewDao: ReviewDao,
    private val bookDao: BookDao,
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ReviewRepository {

    override fun getAllLocalReviews(): Flow<List<Review>> {
        return reviewDao.getAllReviews().map { entities ->
            entities.map { entity ->
                val review = entity.toDomain()

                val associatedBook = bookDao.getBookById(review.bookId)?.toDomain()

                review.copy(book = associatedBook)
            }
        }
    }

    override suspend fun deleteReview(review: Review): Boolean = withContext(ioDispatcher) {
        runCatching {
            val response = reviewApi.deleteReview(review.bookId ?: return@withContext false)

            if (response.isSuccessful) {
                reviewDao.deleteReview(review.toEntity())
                true
            } else {
                Log.e(TAG, "Failed to delete remote review: ${response.code()}")
                false
            }
        }.getOrElse { e ->
            Log.e(TAG, "Error deleting review", e)
            false
        }
    }

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
                val savedReview = response.body()?.toDomain()

                savedReview?.let { domain ->
                    // 因为 API 响应不包含 imageUrls，从原始 review 中拷贝图片路径
                    val entityToSave = domain.toEntity().copy(
                        imageUrls = review.imageUrls.joinToString(",")
                    )
                    reviewDao.insertReview(entityToSave)
                }

                savedReview
            } else {
                Log.e(TAG, "Failed to create review: ${response.code()}")
                null
            }
        }.getOrElse { e ->
            Log.e(TAG, "Error creating review", e)
            null
        }
    }

    override suspend fun uploadReviewImages(uris: List<Uri>): List<String> = withContext(ioDispatcher) {
        val fileNames = uris.mapIndexed { index, _ -> "review-$index.webp" }

        val response = reviewApi.getReviewImageUploadUrls(fileNames)
        if (!response.isSuccessful) return@withContext emptyList()

        val uploadInfos = response.body() ?: return@withContext emptyList()

        uploadInfos.mapIndexed { index, info ->
            try {
                val uri = uris[index]

                val processedBytes = processImageToWebP(uri) ?: return@mapIndexed ""

                val mediaType = "image/webp".toMediaTypeOrNull()
                val requestBody = processedBytes.toRequestBody(mediaType)

                val uploadResponse = reviewApi.uploadImageToS3(info.uploadUrl, requestBody)

                if (uploadResponse.isSuccessful) {
                    "${APIConfig.IMG_REVIEW}/${info.objectKey.removePrefix("/")}"
                } else {
                    val errorBody = uploadResponse.errorBody()?.string()
                    Log.e("R2_UPLOAD", "Failed: ${uploadResponse.code()}, Error: $errorBody")
                    ""
                }
            } catch (e: Exception) {
                Log.e("R2_UPLOAD", "Error", e)
                ""
            }
        }.filter { it.isNotEmpty() }
    }

    private fun processImageToWebP(uri: Uri): ByteArray? {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)

            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->

                val maxWidth = 1024
                if (info.size.width > maxWidth) {
                    val ratio = maxWidth.toFloat() / info.size.width
                    val targetHeight = (info.size.height * ratio).toInt()
                    decoder.setTargetSize(maxWidth, targetHeight)
                }

                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }

            val outputStream = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 75, outputStream)

            val result = outputStream.toByteArray()

            if (!bitmap.isRecycled) bitmap.recycle()

            result
        } catch (e: Exception) {
            Log.e(TAG, "processImageToWebP failed", e)
            null
        }
    }
}