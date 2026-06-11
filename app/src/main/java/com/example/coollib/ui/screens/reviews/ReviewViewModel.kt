package com.example.coollib.ui.screens.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.data.local.SessionManager
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.domain.usecase.ReviewUseCase
import com.example.coollib.telemetry.TelemetryManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase,
    private val sessionManager: SessionManager,
    private val telemetryManager: TelemetryManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val isLoggedIn: Boolean get() = sessionManager.getToken() != null

    val reviews: StateFlow<List<Review>> =
        reviewUseCase.getAllLocalReviews()
            .onStart {
                _isLoading.value = true
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun deleteReview(review: Review) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                reviewUseCase.deleteReview(review)
            }.onSuccess {
                telemetryManager.trackAction(
                    actionName = TelemetryEvents.Actions.BOOK_DELETE_REVIEW_SUCCESS,
                    bookId = review.bookId,
                    extra = mapOf("review_id" to (review.id?.toString() ?: "local"))
                )
            }.onFailure { error ->
                telemetryManager.trackException(
                    actionName = TelemetryEvents.Actions.BOOK_DETAIL_LOAD_FAILURE, // 或者是你后续扩展的 REVIEW_ACTION_FAILURE
                    errorMessage = error.message ?: "Failed to delete reviewId: ${review.id} for bookId: ${review.bookId}"
                )
            }
            _isLoading.value = false
        }
    }
}