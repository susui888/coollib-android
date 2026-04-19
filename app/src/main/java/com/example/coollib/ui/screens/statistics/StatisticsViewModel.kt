package com.example.coollib.ui.screens.statistics

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
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class StatisticsState(
    val currentlyBorrowed: Int = 0,
    val dueSoon: Int = 0,
    val overdue: Int = 0,
    val totalBorrowed: Int = 0,
    val weeklyActivity: List<Float> = List(30) { 0f },
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val loanUseCase: LoanUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        val token = sessionManager.getToken()
        if (token == null) {
            _state.value = StatisticsState(isLoggedIn = false)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isLoggedIn = true)
            try {
                val loans = loanUseCase.getAllLoans()
                val now = LocalDate.now()


                // 1. 获取过去 14 天的日期（2周）
                val last14Days = (0..13).map { now.minusDays(it.toLong()) }.reversed()

                // 2. 统计每天借阅数量
                val dailyCounts = last14Days.map { date ->
                    loans.count { it.borrowDate == date }.toFloat()
                }

                // 3. 归一化 (0.0 ~ 1.0)
                val maxVal = dailyCounts.maxOrNull() ?: 1f
                val normalizedActivity = dailyCounts.map { if (maxVal == 0f) 0f else it / maxVal }


                _state.value = StatisticsState(
                    currentlyBorrowed = loans.count { it.status == LoanStatus.Borrowed },
                    dueSoon = loans.count { 
                        it.status == LoanStatus.Borrowed && 
                        ChronoUnit.DAYS.between(now, it.dueDate) in 0..3 
                    },
                    overdue = loans.count { it.status == LoanStatus.Overdue },
                    totalBorrowed = loans.size,
                    weeklyActivity = normalizedActivity,
                    isLoading = false,
                    isLoggedIn = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}
