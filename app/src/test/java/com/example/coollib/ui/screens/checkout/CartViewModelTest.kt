package com.example.coollib.ui.screens.checkout

import com.example.coollib.data.local.SessionManager
import com.example.coollib.domain.usecase.CartUseCase
import com.example.coollib.ui.previewSupport.MockCart
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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

    // 使用 UnconfinedTestDispatcher 可以让测试中的 Flow 收集立即执行
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // 配置通用的 Mock 行为
        coEvery { cartUseCase.getCartItems() } returns MutableStateFlow(MockCart.list)
        coEvery { cartUseCase.getCartCount() } returns MutableStateFlow(MockCart.list.size)
        coEvery { cartUseCase.isBookInCart(any()) } returns MutableStateFlow(true)
        coEvery { cartUseCase.addToCart(any()) } returns Unit
        coEvery { cartUseCase.removeFromCart(any()) } returns Unit

        // 默认状态设为已登录
        every { sessionManager.getToken() } returns "mock_token"

        viewModel = CartViewModel(cartUseCase, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cartItems should reflect list from use case`() = runTest {
        // Given: cartItems 正在被收集（激活 WhileSubscribed）
        backgroundScope.launch(testDispatcher) { viewModel.cartItems.collect {} }

        // Then
        assertEquals(MockCart.list, viewModel.cartItems.value)
    }

    @Test
    fun `borrowBooks should emit NavigateToLogin when user is not logged in`() = runTest {
        // Given
        every { sessionManager.getToken() } returns null

        // 使用 backgroundScope 自动处理事件收集
        val events = mutableListOf<CartUiEvent>()
        backgroundScope.launch(testDispatcher) {
            viewModel.uiEvent.collect { events.add(it) }
        }

        // When
        viewModel.borrowBooks()

        // Then
        assertEquals(CartUiEvent.NavigateToLogin, events.first())
    }

    @Test
    fun `borrowBooks should call use case when user is logged in and cart is not empty`() = runTest {
        // Given: 激活数据流并设置 Mock
        backgroundScope.launch(testDispatcher) { viewModel.cartItems.collect {} }

        every { sessionManager.getToken() } returns "valid_token"
        coEvery { cartUseCase.borrowBooks(any()) } returns Result.success("Success")

        // When
        viewModel.borrowBooks()

        // Then
        coVerify(exactly = 1) { cartUseCase.borrowBooks(MockCart.list) }
    }

    @Test
    fun `toggleCart should call removeFromCart when book is already in cart`() = runTest {
        // When
        viewModel.toggleCart(bookId = 1, isInCart = true)

        // Then
        coVerify { cartUseCase.removeFromCart(1) }
    }

    @Test
    fun `toggleCart should call addToCart when book is not in cart`() = runTest {
        // When
        viewModel.toggleCart(bookId = 1, isInCart = false)

        // Then
        coVerify { cartUseCase.addToCart(1) }
    }
}