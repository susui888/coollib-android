package com.example.coollib.data.remote

import com.example.coollib.data.local.SessionManager
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        val token = sessionManager.getToken()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}