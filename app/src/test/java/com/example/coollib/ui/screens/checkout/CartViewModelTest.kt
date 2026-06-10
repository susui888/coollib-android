package com.example.coollib.ui.screens.checkout

import com.example.coollib.data.local.SessionManager
import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.domain.usecase.CartUseCase
import com.example.coollib.telemetry.TelemetryManager
import com.example.coollib.ui.previewSupport.MockCart
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
class CartViewModelTest {

    private val cartUseCase: CartUseCase = mockk()
    private val sessionManager: SessionManager = mockk()
    private val telemetryManager: TelemetryManager = mockk(relaxed = true) // 🌟 注入遥测 Mock

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { cartUseCase.getCartItems() } returns MutableStateFlow(MockCart.list)
        coEvery { cartUseCase.getCartCount() } returns MutableStateFlow(MockCart.list.size)
        coEvery { cartUseCase.isBookInCart(any()) } returns MutableStateFlow(true)
        coEvery { cartUseCase.addToCart(any()) } returns Unit
        coEvery { cartUseCase.removeFromCart(any()) } returns Unit

        every { sessionManager.getToken() } returns "mock_token"

        // 🌟 注入遥测管理器
        viewModel = CartViewModel(cartUseCase, sessionManager, telemetryManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cartItems should reflect list from use case`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.cartItems.collect {} }
        assertEquals(MockCart.list, viewModel.cartItems.value)
    }

    @Test
    fun `borrowBooks should emit NavigateToLogin when user is not logged in`() = runTest {
        every { sessionManager.getToken() } returns null

        val events = mutableListOf<CartUiEvent>()
        backgroundScope.launch(testDispatcher) {
            viewModel.uiEvent.collect { events.add(it) }
        }

        viewModel.borrowBooks()

        assertEquals(CartUiEvent.NavigateToLogin, events.first())
    }

    @Test
    fun `borrowBooks should call use case and track success when logged in`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.cartItems.collect {} }
        every { sessionManager.getToken() } returns "valid_token"
        coEvery { cartUseCase.borrowBooks(any()) } returns Result.success("Success")

        viewModel.borrowBooks()

        // 验证业务调用
        coVerify(exactly = 1) { cartUseCase.borrowBooks(MockCart.list) }
        // 🌟 验证遥测：借阅成功追踪
        coVerify(exactly = 1) {
            telemetryManager.trackAction(
                actionName = TelemetryEvents.Actions.BOOK_RENT_ACTION,
                extra = mapOf("cart_items_count" to MockCart.list.size.toString())
            )
        }
    }

    @Test
    fun `toggleCart should call removeFromCart and track action when book is already in cart`() = runTest {
        viewModel.toggleCart(bookId = 1, isInCart = true)

        coVerify { cartUseCase.removeFromCart(1) }
        // 🌟 验证遥测：移除购物车埋点
        coVerify(exactly = 1) {
            telemetryManager.trackAction(TelemetryEvents.Actions.BOOK_REMOVE_CART, bookId = 1)
        }
    }

    @Test
    fun `toggleCart should call addToCart and track action when book is not in cart`() = runTest {
        viewModel.toggleCart(bookId = 1, isInCart = false)

        coVerify { cartUseCase.addToCart(1) }
        // 🌟 验证遥测：加入购物车埋点
        coVerify(exactly = 1) {
            telemetryManager.trackAction(TelemetryEvents.Actions.BOOK_ADD_CART, bookId = 1)
        }
    }
}