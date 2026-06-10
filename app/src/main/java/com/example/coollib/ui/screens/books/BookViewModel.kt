package com.example.coollib.ui.screens.books

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.domain.usecase.BookUseCase
import com.example.coollib.domain.usecase.ReviewUseCase
import com.example.coollib.telemetry.TelemetryManager
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
    private val telemetryManager: TelemetryManager
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

                telemetryManager.trackAction(
                    actionName = TelemetryEvents.Actions.BOOK_SEARCH,
                    extra = mapOf(
                        "search_type" to query.searchType.name,
                        "query_text" to query.toLogText()
                    )
                )
            } catch (e: Exception) {
                _books.value = emptyList()

                telemetryManager.trackException(
                    actionName = TelemetryEvents.Actions.HOME_DATA_LOAD_FAILURE,
                    errorMessage = e.message ?: "Search queries failure for text: ${query.toLogText()}"
                )
            }
        }

    fun selectBook(id: Int) =
        viewModelScope.launch {
            try {
                _selectedBook.value = bookUseCase.getBookById(id)
                loadReviews(id)
            } catch (e: Exception) {
                _selectedBook.value = null

                telemetryManager.trackException(
                    actionName = TelemetryEvents.Actions.BOOK_DETAIL_LOAD_FAILURE,
                    errorMessage = e.message ?: "Fetch book detail failed for bookId: $id"
                )
            }
        }

    private fun loadReviews(bookId: Int) =
        viewModelScope.launch {
            _reviews.value = reviewUseCase.getReviewsByBook(bookId)
        }

    fun postReview(bookId: Int, rating: Int, content: String, imageUris: List<Uri>) {
        viewModelScope.launch {
            val imageUrls = if (imageUris.isNotEmpty()) {
                reviewUseCase.uploadImages(imageUris)
            } else {
                emptyList()
            }

            val newReview = Review(
                id = null,
                bookId = bookId,
                userId = 0,
                userName = "",
                rating = rating,
                content = content,
                createdAt = Instant.now(),
                imageUrls = imageUrls
            )

            val result = reviewUseCase.createReview(newReview)
            if (result != null) {
                loadReviews(bookId)

                telemetryManager.trackAction(
                    actionName = TelemetryEvents.Actions.BOOK_POST_REVIEW_SUCCESS,
                    bookId = bookId,
                    extra = mapOf(
                        "rating" to rating.toString(),
                        "image_count" to imageUris.size.toString()
                    )
                )
            }
        }
    }

}