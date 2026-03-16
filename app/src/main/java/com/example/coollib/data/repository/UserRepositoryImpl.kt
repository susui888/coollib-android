package com.example.coollib.data.repository

import com.example.coollib.data.remote.UserApi
import com.example.coollib.data.remote.UserDto
import com.example.coollib.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun login(username: String, password: String): Map<String, String> {
        val userDto = UserDto(username, password, "")
        return userApi.login(userDto)
    }

    override suspend fun register(username: String, password: String, email: String): String {
        val userDto = UserDto(username, password, email)
        return userApi.register(userDto)
    }
}