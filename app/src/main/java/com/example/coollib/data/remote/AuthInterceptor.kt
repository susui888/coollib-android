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


        val host = request.url.host.lowercase()

        // Security check: Only inject the Authorization header if the target
        // host is within our internal ecosystem (ryansu.uk).
        // This prevents leaking sensitive Bearer tokens to external providers
        // like Cloudflare R2 or Amazon S3 during direct uploads.

        if (host.contains("ryansu.uk")) {
            val token = sessionManager.getToken()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer $token"
                )
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}