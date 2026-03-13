package com.example.coollib.ui.screens.checkout

import com.example.coollib.domain.model.Wishlist
import com.example.coollib.domain.usecase.WishlistUseCase
import com.example.coollib.ui.previewSupport.MockWishlist
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    private lateinit var viewModel: WishlistViewModel
    private val wishlistUseCase: WishlistUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // 设置主线程 Dispatcher
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)

        // Mock 返回值
        val mockWishlistFlow = MutableStateFlow(MockWishlist.list)
        val mockCountFlow = MutableStateFlow(MockWishlist.list.size)

        coEvery { wishlistUseCase.getWishlist() } returns mockWishlistFlow
        coEvery { wishlistUseCase.getWishlistCount() } returns mockCountFlow
        coEvery { wishlistUseCase.addToWishlist(any()) } returns Unit
        coEvery { wishlistUseCase.removeFromWishlist(any()) } returns Unit
        coEvery { wishlistUseCase.isBookInWishlist(any()) } returns MutableStateFlow(true)

        viewModel = WishlistViewModel(wishlistUseCase)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun wishlist_returnsStateFlow() = runTest {
        val items = mutableListOf<List<Wishlist>>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.wishlist.collect { items.add(it) }
        }

        // 断言最终发射的值等于 MockWishlist.list
        assertEquals(MockWishlist.list, items.last())

        job.cancel()
    }

    @Test
    fun wishlistCount_returnsStateFlow() = runTest {
        val counts = mutableListOf<Int>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.wishlistCount.collect { counts.add(it) }
        }

        assertEquals(MockWishlist.list.size, counts.last())

        job.cancel()
    }

    @Test
    fun isBookInWishlist_returnsFlow() = runTest {
        val firstItem = MockWishlist.list.first()
        val value = viewModel.isBookInWishlist(firstItem.id).first()
        assertEquals(true, value)
    }

    @Test
    fun toggleWishlist_addsBookWhenNotInWishlist() = runTest {
        val firstItem = MockWishlist.list.first()
        viewModel.toggleWishlist(firstItem.id, isInWishlist = false)
        coVerify { wishlistUseCase.addToWishlist(firstItem.id) }
    }

    @Test
    fun toggleWishlist_removesBookWhenInWishlist() = runTest {
        val firstItem = MockWishlist.list.first()
        viewModel.toggleWishlist(firstItem.id, isInWishlist = true)
        coVerify { wishlistUseCase.removeFromWishlist(firstItem.id) }
    }

    @Test
    fun removeFromWishlist_callsUseCase() = runTest {
        val firstItem = MockWishlist.list.first()
        viewModel.removeFromWishlist(firstItem.id)
        coVerify { wishlistUseCase.removeFromWishlist(firstItem.id) }
    }
}