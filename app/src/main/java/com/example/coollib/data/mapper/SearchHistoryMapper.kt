package com.example.coollib.data.mapper

import com.example.coollib.data.local.SearchHistoryEntity
import com.example.coollib.domain.model.SearchHistory

fun SearchHistoryEntity.toDomain() =
    SearchHistory(
        this.keyword
    )

fun SearchHistory.toEntity() =
    SearchHistoryEntity(
        this.keyword
    )