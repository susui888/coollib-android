package com.example.coollib.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toDto
import com.example.coollib.data.remote.APIConfig
import com.example.coollib.data.remote.ReviewApi
import com.example.coollib.di.IoDispatcher
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.repository.ReviewRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val TAG = "ReviewRepository"

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    @param:ApplicationContext private val context: Context,
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

    override suspend fun uploadReviewImages(uris: List<Uri>): List<String> = withContext(ioDispatcher) {
        val fileNames = uris.map { it.getFileName(context) }
        val response = reviewApi.getReviewImageUploadUrls(fileNames)
        if (!response.isSuccessful) return@withContext emptyList()

        val uploadInfos = response.body() ?: return@withContext emptyList()


        uploadInfos.mapIndexed { index, info ->
            try {
                val uri = uris[index]
                val rawMimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                val pureMimeType = rawMimeType.split(";")[0].lowercase()

                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.use { it.readBytes() } ?: return@mapIndexed ""

                val mediaType = pureMimeType.toMediaTypeOrNull()
                val requestBody = bytes.toRequestBody(mediaType)

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
}

fun Uri.getFileName(context: Context): String {
    val contentResolver = context.contentResolver
    val type = contentResolver.getType(this)
    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(type)
    return "review_${System.currentTimeMillis()}.${extension ?: "jpg"}"
}