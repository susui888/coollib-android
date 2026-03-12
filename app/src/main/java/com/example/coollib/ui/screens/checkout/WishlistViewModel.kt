package com.example.coollib.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Wishlist
import com.example.coollib.domain.usecase.WishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistUseCase: WishlistUseCase
) : ViewModel() {

    fun toggleWishlist(bookId: Int, isInWishlist: Boolean) {
        viewModelScope.launch {
            runCatching {
                if (isInWishlist) {
                    wishlistUseCase.removeFromWishlist(bookId)
                } else {
                    wishlistUseCase.addToWishlist(bookId)
                }
            }
        }
    }

    fun isBookInWishlist(bookId: Int): Flow<Boolean> =
        wishlistUseCase.isBookInWishlist(bookId)

    val wishlist: StateFlow<List<Wishlist>> =
        wishlistUseCase.getWishlist()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val wishlistCount: StateFlow<Int> =
        wishlistUseCase.getWishlistCount()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    fun removeFromWishlist(bookId: Int) {
        viewModelScope.launch {
            wishlistUseCase.removeFromWishlist(bookId)
        }
    }
}