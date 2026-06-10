package com.example.coollib.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception

class TelemetryInterceptor : Interceptor {

    private val backgroundScope = CoroutineScope(Dispatchers.IO)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlString = request.url.toString()

        // 环路保护：如果是遥测本身发出的请求，直接放行
        if (urlString.contains("mobile-telemetry")) {
            return chain.proceed(request)
        }

        val startTime = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            trackNetworkFailure(urlString, request.method, -1, e.localizedMessage ?: "Network Connection Error")
            throw e
        }

        val endTime = System.nanoTime()
        val latencyMs = ((endTime - startTime) / 1_000_000).toInt()
        val statusCode = response.code
        val endpointPath = request.url.encodedPath

        // 异步派发度量指标，非阻塞主业务网络响应
        backgroundScope.launch {
            try {
                TelemetryApi.globalDispatcher?.recordMetric(
                    ApiMetricDto(
                        endpoint = endpointPath,
                        method = request.method,
                        statusCode = statusCode,
                        latencyMs = latencyMs
                    )
                )
            } catch (_: Exception) {}
        }

        // 拦截非 2xx/3xx HTTP 错误
        if (!response.isSuccessful) {
            trackNetworkFailure(urlString, request.method, statusCode, "HTTP Error Status")
        }

        return response
    }

    private fun trackNetworkFailure(url: String, method: String, statusCode: Int, message: String) {
        val endpointPath = try { java.net.URL(url).path } catch (_: Exception) { url }
        backgroundScope.launch {
            try {
                TelemetryApi.globalDispatcher?.recordEvent(
                    TelemetryEventDto(
                        eventType = "ERROR",
                        eventName = "API_Network_Failure",
                        errorMessage = message,
                        attributes = mapOf(
                            "endpoint" to endpointPath,
                            "method" to method,
                            "status_code" to statusCode.toString()
                        )
                    )
                )
            } catch (_: Exception) {}
        }
    }
}