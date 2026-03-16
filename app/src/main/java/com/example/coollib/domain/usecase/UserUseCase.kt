package com.example.coollib.domain.usecase

import com.example.coollib.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun login(username: String, password: String) =
        userRepository.login(username, password)

    suspend fun register(username: String, password: String, email: String) : String =
        userRepository.register(username, password, email)
}