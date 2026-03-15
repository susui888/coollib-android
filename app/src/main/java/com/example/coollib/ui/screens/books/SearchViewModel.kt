package com.example.coollib.ui.screens.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.SearchHistory
import com.example.coollib.domain.usecase.SearchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase
) : ViewModel() {

    val history: StateFlow<List<SearchHistory>> =
        searchHistoryUseCase.getHistory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addSearch(keyword: String) = viewModelScope.launch {
        searchHistoryUseCase.add(keyword)
    }

    fun deleteSearch(keyword: String) = viewModelScope.launch {
        searchHistoryUseCase.delete(keyword)
    }

    fun clearHistory() = viewModelScope.launch {
        searchHistoryUseCase.clear()
    }
}