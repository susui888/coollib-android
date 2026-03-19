package com.example.coollib.data.repository

import android.util.Log
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.remote.LoanApi
import com.example.coollib.di.IoDispatcher
import com.example.coollib.domain.model.Loan
import com.example.coollib.domain.repository.BookRepository
import com.example.coollib.domain.repository.LoanRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

private const val TAG = "LoanRepository"

class LoanRepositoryImpl @Inject constructor(
    private val loanApi: LoanApi,
    private val bookRepository: BookRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LoanRepository {

    override suspend fun getAllLoans(): List<Loan> = withContext(ioDispatcher) {
        runCatching {
            val response = loanApi.getAllLoans()

            if (!response.isSuccessful) {
                Log.e(TAG, "API request failed with code: ${response.code()}")
                return@withContext emptyList()
            }

            val loanDtos = response.body() ?: return@withContext emptyList()

            // Fetch book details in parallel to improve conversion efficiency
            loanDtos.map { loanDto ->
                async {
                    val book = bookRepository.getBookById(loanDto.bookId)
                    loanDto.toDomain(book)
                }
            }.awaitAll()
        }.getOrElse { e ->
            Log.e(TAG, "Error occurred while fetching loan records", e)
            emptyList()
        }
    }
}
