package com.example.coollib.domain.usecase

import com.example.coollib.domain.model.Loan
import com.example.coollib.domain.repository.LoanRepository
import javax.inject.Inject

class LoanUseCase @Inject constructor(
    private val repository: LoanRepository
) {
    suspend fun getAllLoans(): List<Loan> = repository.getAllLoans()
}
