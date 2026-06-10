package com.example.coollib.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class TelemetryEventDto(
    @property:Json(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @property:Json(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @property:Json(name = "platform")
    val platform: String = "Android",

    @property:Json(name = "event_type")
    val eventType: String,

    @property:Json(name = "event_name")
    val eventName: String,

    @property:Json(name = "app_version")
    val appVersion: String = "1.0.0",

    @property:Json(name = "error_message")
    val errorMessage: String?,

    @property:Json(name = "attributes")
    val attributes: Map<String, String>?
)

@JsonClass(generateAdapter = true)
data class ApiMetricDto(
    @property:Json(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @property:Json(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @property:Json(name = "platform")
    val platform: String = "Android",

    @property:Json(name = "endpoint")
    val endpoint: String,

    @property:Json(name = "method")
    val method: String,

    @property:Json(name = "status_code")
    val statusCode: Int,

    @property:Json(name = "latency_ms")
    val latencyMs: Int
)

data class EmptyResponse(
    val message: String = ""
)