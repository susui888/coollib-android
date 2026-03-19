package com.example.coollib.domain.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class Loan(
    var id: Int,
    var book: Book?,
    var borrowDate: LocalDate,
    var dueDate: LocalDate,
    var returnDate: LocalDate? = null,
    var status: LoanStatus,
)

enum class LoanStatus(
    val description: String,
    val displayColor: Color
) {
    Borrowed("Borrowed", Color(0xFF00A0FF)),
    Returned("Returned", Color(0xFF2ECC71)),
    Overdue("Overdue", Color(0xFFE74C3C));

    fun isFinished() = this == Returned

    fun getUserFriendlyDescription() = description

}