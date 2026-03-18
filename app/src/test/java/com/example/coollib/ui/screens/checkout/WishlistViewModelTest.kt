package com.example.coollib.ui.screens.checkout

import com.example.coollib.domain.usecase.WishlistUseCase
import com.example.coollib.ui.previewSupport.MockWishlist
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    private val wishlistUseCase: WishlistUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: WishlistViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mock Default Behaviors
        coEvery { wishlistUseCase.getWishlist() } returns MutableStateFlow(MockWishlist.list)
        coEvery { wishlistUseCase.getWishlistCount() } returns MutableStateFlow(MockWishlist.list.size)
        coEvery { wishlistUseCase.isBookInWishlist(any()) } returns MutableStateFlow(true)
        coEvery { wishlistUseCase.addToWishlist(any()) } returns Unit
        coEvery { wishlistUseCase.removeFromWishlist(any()) } returns Unit

        viewModel = WishlistViewModel(wishlistUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `wishlist should reflect items from use case`() = runTest {
        // Given: Active collection to trigger WhileSubscribed
        backgroundScope.launch(testDispatcher) { viewModel.wishlist.collect {} }

        // Then
        assertEquals(MockWishlist.list, viewModel.wishlist.value)
    }

    @Test
    fun `wishlistCount should reflect count from use case`() = runTest {
        // Given: Active collection
        backgroundScope.launch(testDispatcher) { viewModel.wishlistCount.collect {} }

        // Then
        assertEquals(MockWishlist.list.size, viewModel.wishlistCount.value)
    }

    @Test
    fun `toggleWishlist should call removeFromWishlist when book is already in wishlist`() = runTest {
        // When
        viewModel.toggleWishlist(bookId = 1, isInWishlist = true)

        // Then
        coVerify(exactly = 1) { wishlistUseCase.removeFromWishlist(1) }
    }

    @Test
    fun `toggleWishlist should call addToWishlist when book is not in wishlist`() = runTest {
        // When
        viewModel.toggleWishlist(bookId = 1, isInWishlist = false)

        // Then
        coVerify(exactly = 1) { wishlistUseCase.addToWishlist(1) }
    }

    @Test
    fun `removeFromWishlist should call use case`() = runTest {
        // When
        viewModel.removeFromWishlist(bookId = 1)

        // Then
        coVerify(exactly = 1) { wishlistUseCase.removeFromWishlist(1) }
    }
}
