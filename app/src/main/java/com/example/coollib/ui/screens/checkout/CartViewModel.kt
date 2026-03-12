package com.example.coollib.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.usecase.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase
) : ViewModel() {

    fun toggleCart(bookId: Int, isInCart: Boolean) {
        viewModelScope.launch {
            runCatching {
                if (isInCart) {
                    cartUseCase.removeFromCart(bookId)
                } else {
                    cartUseCase.addToCart(bookId)
                }
            }
        }
    }

    fun isBookInCart(bookId: Int): Flow<Boolean> =
        cartUseCase.isBookInCart(bookId)

    val cartItems: StateFlow<List<Cart>> =
        cartUseCase.getCartItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val cartCount: StateFlow<Int> =
        cartItems
            .map { it.size }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    fun removeFromCart(bookId: Int) {
        viewModelScope.launch {
            cartUseCase.removeFromCart(bookId)
        }
    }
}