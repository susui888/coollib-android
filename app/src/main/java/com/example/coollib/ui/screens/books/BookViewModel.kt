package com.example.coollib.ui.screens.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.usecase.BookUseCase
import com.example.coollib.domain.usecase.ReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookUseCase: BookUseCase,
    private val reviewUseCase: ReviewUseCase,
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    fun searchBooks(query: SearchQuery) =
        viewModelScope.launch {
            try {
                _books.value = bookUseCase.searchBooks(query)
            } catch (e: Exception) {
                _books.value = emptyList()
            }
        }

    fun selectBook(id: Int) =
        viewModelScope.launch {
            try {
                _selectedBook.value = bookUseCase.getBookById(id)
                loadReviews(id)
            } catch (e: Exception) {
                _selectedBook.value = null
            }
        }

    private fun loadReviews(bookId: Int) =
        viewModelScope.launch {
            _reviews.value = reviewUseCase.getReviewsByBook(bookId)
        }

    fun postReview(bookId: Int, rating: Int, content: String) {
        viewModelScope.launch {
            val newReview = Review(
                id = null,
                bookId = bookId,
                userId = 0,
                rating = rating,
                content = content,
                createdAt = Instant.now()
            )

            val result = reviewUseCase.createReview(newReview)
            if (result != null) {
                loadReviews(bookId)
            }
        }
    }

}