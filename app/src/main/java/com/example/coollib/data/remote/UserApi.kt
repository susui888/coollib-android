package com.example.coollib.data.remote


import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: UserDto      // 只使用 username + password, email 可以填空 ""
    ): Map<String, String>          // 返回 {"token": "...", "username": "..."}

    @POST("auth/register")
    suspend fun register(
        @Body request: UserDto      // username, password, email 全部使用
    ): String                       // 返回 "User registered successfully"
}