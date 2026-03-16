package com.example.coollib.domain.repository


interface UserRepository {

    suspend fun login(username: String, password: String): Map<String, String>

    suspend fun register(username: String, password: String, email: String): String
}