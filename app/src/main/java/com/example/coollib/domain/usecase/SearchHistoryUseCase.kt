package com.example.coollib.domain.usecase

import com.example.coollib.domain.model.SearchHistory
import com.example.coollib.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    fun getHistory(): Flow<List<SearchHistory>> =
        repository.getRecentSearch()

    suspend fun add(keyword: String) =
        repository.addSearch(keyword)

    suspend fun delete(keyword: String) =
        repository.deleteSearch(keyword)

    suspend fun clear() =
        repository.clearHistory()
}