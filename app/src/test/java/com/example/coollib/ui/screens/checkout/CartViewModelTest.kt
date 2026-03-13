package com.example.coollib.ui.screens.checkout

import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.usecase.CartUseCase
import com.example.coollib.ui.previewSupport.MockCart
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val cartUseCase: CartUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // 🔹 Mock 返回值
        val mockCartFlow = MutableStateFlow(MockCart.list)
        val mockCountFlow = MutableStateFlow(MockCart.list.size)

        coEvery { cartUseCase.getCartItems() } returns mockCartFlow
        coEvery { cartUseCase.getCartCount() } returns mockCountFlow

        coEvery { cartUseCase.addToCart(any()) } returns Unit
        coEvery { cartUseCase.removeFromCart(any()) } returns Unit
        coEvery { cartUseCase.isBookInCart(any()) } returns MutableStateFlow(true)

        viewModel = CartViewModel(cartUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -------------------- 测试 StateFlow --------------------

    @Test
    fun cartItems_returnsStateFlow() = runTest {
        val items = mutableListOf<List<Cart>>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.cartItems.collect { items.add(it) }
        }
        assertEquals(MockCart.list, items.last())
        job.cancel()
    }

    @Test
    fun cartCount_returnsStateFlow() = runTest {
        val counts = mutableListOf<Int>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.cartCount.collect { counts.add(it) }
        }
        assertEquals(MockCart.list.size, counts.last())
        job.cancel()
    }

    @Test
    fun isBookInCart_returnsFlow() = runTest {
        val firstItem = MockCart.list.first()
        val value = viewModel.isBookInCart(firstItem.id).first()  // 取一次即可
        assertEquals(true, value)
    }

    // -------------------- 测试 toggleCart --------------------

    @Test
    fun toggleCart_addsBookWhenNotInCart() = runTest {
        val firstItem = MockCart.list.first()
        viewModel.toggleCart(firstItem.id, isInCart = false)
        coVerify { cartUseCase.addToCart(firstItem.id) }
    }

    @Test
    fun toggleCart_removesBookWhenInCart() = runTest {
        val firstItem = MockCart.list.first()
        viewModel.toggleCart(firstItem.id, isInCart = true)
        coVerify { cartUseCase.removeFromCart(firstItem.id) }
    }

    @Test
    fun removeFromCart_callsUseCase() = runTest {
        val firstItem = MockCart.list.first()
        viewModel.removeFromCart(firstItem.id)
        coVerify { cartUseCase.removeFromCart(firstItem.id) }
    }
}