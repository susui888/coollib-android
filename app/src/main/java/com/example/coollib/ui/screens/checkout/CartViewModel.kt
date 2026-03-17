package com.example.coollib.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.usecase.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase
) : ViewModel() {

    // UI State: Handle loading, etc.
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    // UI Event: Handle one-time events like Snackbar, Navigation, etc.
    private val _uiEvent = MutableSharedFlow<CartUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    val cartItems: StateFlow<List<Cart>> =
        cartUseCase.getCartItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    
    val cartCount: StateFlow<Int> =
        cartUseCase.getCartCount()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    fun toggleCart(bookId: Int, isInCart: Boolean) {
        viewModelScope.launch {
            if (isInCart) {
                cartUseCase.removeFromCart(bookId)
            } else {
                cartUseCase.addToCart(bookId)
            }
        }
    }

    fun removeFromCart(bookId: Int) {
        viewModelScope.launch {
            cartUseCase.removeFromCart(bookId)
        }
    }

    // Execute borrow logic
    fun borrowBooks() {
        val currentItems = cartItems.value
        if (currentItems.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            cartUseCase.borrowBooks(currentItems)
                .onSuccess { message ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(CartUiEvent.ShowSnackbar(message))
                    _uiEvent.emit(CartUiEvent.NavigateBack) 
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(CartUiEvent.ShowSnackbar(error.message ?: "Borrow failed"))
                }
        }
    }

    fun isBookInCart(bookId: Int): Flow<Boolean> =
        cartUseCase.isBookInCart(bookId)
}

data class CartUiState(
    val isLoading: Boolean = false
)

sealed class CartUiEvent {
    data class ShowSnackbar(val message: String) : CartUiEvent()
    object NavigateBack : CartUiEvent()
}