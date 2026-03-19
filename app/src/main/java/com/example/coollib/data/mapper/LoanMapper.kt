package com.example.coollib.data.mapper

import com.example.coollib.data.remote.LoanDto
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Loan

fun LoanDto.toDomain(theBook: Book?) = Loan(
    id = this.id,
    book = theBook,
    borrowDate = this.borrowDate,
    dueDate = this.dueDate,
    returnDate = this.returnDate,
    status = this.status,
)
