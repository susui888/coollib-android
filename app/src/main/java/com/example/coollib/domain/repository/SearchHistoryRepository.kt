package com.example.coollib.domain.repository

import com.example.coollib.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun getRecentSearch(): Flow<List<SearchHistory>>

    suspend fun addSearch(keyword: String)

    suspend fun deleteSearch(keyword: String)

    suspend fun clearHistory()
}