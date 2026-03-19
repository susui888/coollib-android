package com.example.coollib.ui.screens.statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.data.local.SessionManager
import com.example.coollib.domain.model.Loan
import com.example.coollib.domain.model.LoanStatus
import com.example.coollib.domain.usecase.LoanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanViewModel @Inject constructor(
    private val loanUseCase: LoanUseCase,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val filter: String? = savedStateHandle["filter"]

    private val _loans = MutableStateFlow<List<Loan>>(emptyList())
    val loans: StateFlow<List<Loan>> = _loans.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        checkLoginAndLoadLoans()
    }

    fun checkLoginAndLoadLoans() {
        val token = sessionManager.getToken()
        _isLoggedIn.value = token != null
        
        if (_isLoggedIn.value) {
            loadLoans()
        } else {
            _loans.value = emptyList()
        }
    }

    private fun loadLoans() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allLoans = loanUseCase.getAllLoans()
                _loans.value = when (filter) {
                    "borrowed" -> allLoans.filter { it.status == LoanStatus.Borrowed }
                    "history" -> allLoans.filter { it.status == LoanStatus.Returned || it.status == LoanStatus.Overdue }
                    else -> allLoans
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
