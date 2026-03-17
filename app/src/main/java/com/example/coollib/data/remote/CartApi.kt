package com.example.coollib.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CartApi {
    @POST("cart/borrow")
    suspend fun borrowBooks(
        @Body carts: List<CartDto>
    ): Response<BorrowResponse>
}

data class BorrowResponse(
    val status: String,   // 对应后端 Map 里的 "status"
    val message: String   // 对应后端 Map 里的 "message"
)