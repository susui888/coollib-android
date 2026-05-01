package com.example.coollib.data.remote

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ReviewApi {

    @GET("reviews/{bookId}")
    suspend fun getReviewsByBook(
        @Path("bookId") bookId: Int
    ): Response<List<ReviewDto>>

    @POST("reviews")
    suspend fun createReview(
        @Body review: ReviewDto
    ): Response<ReviewDto>

    @GET("reviews/upload-urls")
    suspend fun getReviewImageUploadUrls(
        @Query("fileNames") fileNames: List<String>
    ): Response<List<UploadUrlResponse>>

    @PUT
    suspend fun uploadImageToS3(
        @Url url: String,
        @Body image: RequestBody
    ): Response<Unit>
}

data class UploadUrlResponse(
    val uploadUrl: String,  // 对应服务器返回的预签名上传地址
    val objectKey: String   // 对应服务器返回的 S3 存储路径
)