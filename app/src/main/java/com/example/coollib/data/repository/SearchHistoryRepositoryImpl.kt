package com.example.coollib.data.repository

import com.example.coollib.data.local.SearchHistoryDao
import com.example.coollib.data.local.SearchHistoryEntity
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toEntity
import com.example.coollib.domain.model.SearchHistory
import com.example.coollib.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val dao: SearchHistoryDao
) : SearchHistoryRepository {

    override fun getRecentSearch(): Flow<List<SearchHistory>> =
        dao.getRecentSearch().map { list -> list.map { it.toDomain() } }


    override suspend fun addSearch(keyword: String) {
        val history = SearchHistory(keyword = keyword)
        dao.insert(history.toEntity())
    }

    override suspend fun deleteSearch(keyword: String) =
        dao.delete(keyword)


    override suspend fun clearHistory() =
        dao.clear()
}