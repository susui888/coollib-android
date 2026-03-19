package com.example.coollib.ui.previewSupport

import com.example.coollib.domain.model.Loan
import com.example.coollib.domain.model.LoanStatus
import java.time.LocalDate

object MockLoans {
    val list = listOf(
        Loan(
            id = 1,
            book = MockBooks.list[0],
            borrowDate = LocalDate.now().minusDays(10),
            dueDate = LocalDate.now().plusDays(4),
            status = LoanStatus.Borrowed
        ),
        Loan(
            id = 2,
            book = MockBooks.list[1],
            borrowDate = LocalDate.now().minusDays(20),
            dueDate = LocalDate.now().minusDays(6),
            returnDate = LocalDate.now().minusDays(7),
            status = LoanStatus.Returned
        ),
        Loan(
            id = 3,
            book = MockBooks.list[2],
            borrowDate = LocalDate.now().minusDays(30),
            dueDate = LocalDate.now().minusDays(16),
            status = LoanStatus.Overdue
        )
    )
}
