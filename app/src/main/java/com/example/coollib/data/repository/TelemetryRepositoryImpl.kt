package com.example.coollib.data.repository

import com.example.coollib.data.remote.EmptyResponse
import com.example.coollib.data.remote.TelemetryApi
import com.example.coollib.data.remote.TelemetryEventDto
import com.example.coollib.domain.repository.TelemetryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelemetryRepositoryImpl @Inject constructor(
    private val telemetryApi: TelemetryApi
) : TelemetryRepository {

    init {
        // 在 Hilt 组装单例完成的第一时间将引用注入全局静态槽，完成解耦
        TelemetryApi.globalDispatcher = telemetryApi
    }

    override suspend fun trackScreenView(screenName: String, referrer: String?): EmptyResponse {
        val dto = TelemetryEventDto(
            eventType = "SCREEN_VIEW",
            eventName = screenName,
            errorMessage = null,
            attributes = referrer?.let { mapOf("referrer_source" to it) }
        )
        return telemetryApi.recordEvent(dto)
    }

    override suspend fun trackCustomAction(actionName: String, attributes: Map<String, String>?): EmptyResponse {
        val dto = TelemetryEventDto(
            eventType = "CUSTOM",
            eventName = actionName,
            errorMessage = null,
            attributes = attributes
        )
        return telemetryApi.recordEvent(dto)
    }
}