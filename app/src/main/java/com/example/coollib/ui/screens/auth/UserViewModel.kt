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

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result: Result<Pair<String, String>> = try {
                val response = userUseCase.login(username, password)
                val token = response["token"] ?: throw Exception("No token in response")
                val usernameResp = response["username"] ?: username

                sessionManager.saveToken(token, usernameResp)

                Log.i(TAG, "Token Saved. username: $usernameResp, toke: $token")

                Result.success(token to usernameResp)
            } catch (e: HttpException) {
                val errorMsg = e.response()?.errorBody()?.string() ?: e.message()

                Log.i(TAG, "HttpException:  $errorMsg")

                Result.failure(Exception(errorMsg))
            } catch (e: Exception) {
                Log.i(TAG, "Unknown Exception:  ${e.message}")

                Result.failure(e)
            }

            _loginResult.value = result
        }
    }

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch {
            val result: Result<String> = try {
                val message = userUseCase.register(username, password, email)
                Result.success(message)
            } catch (e: HttpException) {
                val errorMsg = e.response()?.errorBody()?.string() ?: e.message()
                Result.failure(Exception(errorMsg))
            } catch (e: Exception) {
                Result.failure(e)
            }

            _registerResult.value = result
        }
    }

    fun clearLoginResult() {
        _loginResult.value = null
    }

    fun clearRegisterResult() {
        _registerResult.value = null
    }
}