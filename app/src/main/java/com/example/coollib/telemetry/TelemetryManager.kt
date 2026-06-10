package com.example.coollib.telemetry

import com.example.coollib.domain.usecase.TelemetryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelemetryManager @Inject constructor(
    private val telemetryUseCase: TelemetryUseCase
) {
    private val telemetryScope = CoroutineScope(Dispatchers.IO)

    fun trackAction(actionName: String, bookId: Int? = null, extra: Map<String, String>? = null) {
        telemetryScope.launch {
            val attributes = mutableMapOf<String, String>().apply {
                bookId?.let { put("book_id", it.toString()) }
                extra?.let { putAll(it) }
            }

            telemetryUseCase.trackCustomAction(actionName, attributes.ifEmpty { null })
        }
    }

    fun trackException(actionName: String, errorMessage: String?) {
        telemetryScope.launch {
            val attributes = errorMessage?.let { mapOf("error_message" to it) }
            telemetryUseCase.trackCustomAction(actionName, attributes)
        }
    }
}