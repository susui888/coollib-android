package com.example.coollib.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface TelemetryApi {

    @POST("mobile-telemetry/events")
    suspend fun recordEvent(
        @Body request: TelemetryEventDto
    ): EmptyResponse

    @POST("mobile-telemetry/metrics")
    suspend fun recordMetric(
        @Body request: ApiMetricDto
    ): EmptyResponse

    companion object {
        /**
         * 🌟 绕过 OkHttp Interceptor 与 TelemetryApi 相互循环依赖的静态广播槽
         * 对应 iOS 端 nonisolated(unsafe) static var globalDispatcher
         */
        @Volatile
        var globalDispatcher: TelemetryApi? = null
    }
}