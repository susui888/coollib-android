package com.example.coollib.domain.usecase

import com.example.coollib.domain.repository.TelemetryRepository
import javax.inject.Inject

class TelemetryUseCase @Inject constructor(
    private val telemetryRepository: TelemetryRepository
) {
    suspend fun trackScreenView(screenName: String, referrer: String? = null) =
        runCatching { telemetryRepository.trackScreenView(screenName, referrer) }

    suspend fun trackCustomAction(actionName: String, attributes: Map<String, String>? = null) =
        runCatching { telemetryRepository.trackCustomAction(actionName, attributes) }
}