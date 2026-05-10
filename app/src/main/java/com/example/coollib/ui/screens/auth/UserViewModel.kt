package com.example.coollib.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.data.local.SessionManager
import com.example.coollib.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "UserViewModel"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<Pair<String, String>>?>(null)
    val loginResult: StateFlow<Result<Pair<String, String>>?> = _loginResult

    private val _registerResult = MutableStateFlow<Result<String>?>(null)
    val registerResult: StateFlow<Result<String>?> = _registerResult

    private suspend fun performLogin(username: String, password: String): Result<Pair<String, String>> {
        return try {
            val response = userUseCase.login(username, password)
            val token = response["token"] ?: throw Exception("No token in response")
            val usernameResp = response["username"] ?: username

            sessionManager.saveToken(token, usernameResp)
            Log.i(TAG, "Token Saved. username: $usernameResp, token: $token")

            Result.success(token to usernameResp)
        } catch (e: HttpException) {
            val errorMsg = e.response()?.errorBody()?.string() ?: e.message()
            Log.e(TAG, "HttpException: $errorMsg")
            Result.failure(Exception(errorMsg))
        } catch (e: Exception) {
            Log.e(TAG, "Unknown Exception: ${e.message}")
            Result.failure(e)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = performLogin(username, password)
        }
    }

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch {
            try {
                val message = userUseCase.register(username, password, email)
                _registerResult.value = Result.success(message)

                Log.i(TAG, "Register success, attempting auto-login for: $username")
                _loginResult.value = performLogin(username, password)

            } catch (e: HttpException) {
                val errorMsg = e.response()?.errorBody()?.string() ?: e.message()
                _registerResult.value = Result.failure(Exception(errorMsg))
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }

    fun clearLoginResult() {
        _loginResult.value = null
    }

    fun clearRegisterResult() {
        _registerResult.value = null
    }
}