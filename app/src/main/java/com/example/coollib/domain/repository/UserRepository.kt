package com.example.coollib.domain.repository

import com.example.coollib.data.remote.MessageResponse


interface UserRepository {

    suspend fun login(username: String, password: String): Map<String, String>

    suspend fun register(username: String, password: String, email: String): MessageResponse
}