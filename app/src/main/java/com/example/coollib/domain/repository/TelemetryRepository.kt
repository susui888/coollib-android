package com.example.coollib.domain.repository

import com.example.coollib.data.remote.EmptyResponse

interface TelemetryRepository {
    suspend fun trackScreenView(screenName: String, referrer: String?): EmptyResponse

    suspend fun trackCustomAction(actionName: String, attributes: Map<String, String>?): EmptyResponse
}