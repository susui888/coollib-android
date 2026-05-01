package com.example.coollib.data.remote

import com.example.coollib.data.local.SessionManager
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        // 1. 检查请求的主机名
        // 如果请求是发往 Cloudflare R2 的，直接跳过添加 Authorization Header
        if (request.url.host.contains("cloudflarestorage.com")) {
            return chain.proceed(request)
        }

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