package com.example.coollib.domain.repository

import com.example.coollib.domain.model.Loan

interface LoanRepository {
    suspend fun getAllLoans(): List<Loan>
}