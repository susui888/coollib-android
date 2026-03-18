package com.example.coollib.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.usecase.BookUseCase
import com.example.coollib.domain.usecase.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val bookUseCase: BookUseCase,
    private val cartUseCase: CartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ScanUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var lastScannedIsbn: String? = null
    private var lastScanTime: Long = 0

    fun processIsbn(isbn: String) {
        val currentTime = System.currentTimeMillis()
        
        // 1. 防止在加载过程中重复触发
        if (_uiState.value.isLoading) return
        
        // 2. 冷却机制：如果同一个 ISBN 在 5 秒内扫描过，则跳过，避免 404 后立即再次请求
        if (isbn == lastScannedIsbn && currentTime - lastScanTime < 5_000) return

        lastScannedIsbn = isbn
        lastScanTime = currentTime

        viewModelScope.launch {
            _uiState.value = ScanUiState(isLoading = true)

            try {
                val book = bookUseCase.getBookByIsbn(isbn)

                if (book != null) {
                    cartUseCase.addToCart(book.id)
                    _uiState.value = ScanUiState(isLoading = false, detectedBookTitle = book.title)
                    _uiEvent.emit(ScanUiEvent.NavigateToCart)
                } else {
                    _uiState.value = ScanUiState(isLoading = false, error = "Book not found")
                    _uiEvent.emit(ScanUiEvent.ShowError("Book with ISBN $isbn not found"))
                }
            } catch (e: Exception) {
                _uiState.value = ScanUiState(isLoading = false, error = "Book not found")
                _uiEvent.emit(ScanUiEvent.ShowError("Book with ISBN $isbn not found"))
            }
        }
    }
}

data class ScanUiState(
    val isLoading: Boolean = false,
    val detectedBookTitle: String? = null,
    val error: String? = null
)

sealed class ScanUiEvent {
    object NavigateToCart : ScanUiEvent()
    data class ShowError(val message: String) : ScanUiEvent()
}
