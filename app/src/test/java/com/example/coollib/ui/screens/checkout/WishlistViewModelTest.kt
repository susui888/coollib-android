package com.example.coollib.ui.screens.checkout

import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.domain.usecase.WishlistUseCase
import com.example.coollib.telemetry.TelemetryManager // 🌟 引入遥测管理器
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
    private val telemetryManager: TelemetryManager =
        mockk(relaxed = true) // 🌟 创建 relaxed mock 隔离遥测流
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

        // 🌟 注入依赖闭环
        viewModel = WishlistViewModel(wishlistUseCase, telemetryManager)
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

    // ==================================================================================
    // 🔔 心愿单交互与影子追踪测试集 (Wishlist Interaction & Telemetry Tracking)
    // ==================================================================================

    @Test
    fun `toggleWishlist should call removeFromWishlist and track action when book is already in wishlist`() =
        runTest {
            // When
            viewModel.toggleWishlist(bookId = 1, isInWishlist = true)

            // Then
            coVerify(exactly = 1) { wishlistUseCase.removeFromWishlist(1) }
            // 🌟 核心断言：验证移出心愿单的交互行为是否精确外传
            coVerify(exactly = 1) {
                telemetryManager.trackAction(
                    actionName = TelemetryEvents.Actions.BOOK_REMOVE_WISHLIST,
                    bookId = 1
                )
            }
        }

    @Test
    fun `toggleWishlist should call addToWishlist and track action when book is not in wishlist`() =
        runTest {
            // When
            viewModel.toggleWishlist(bookId = 2, isInWishlist = false)

            // Then
            coVerify(exactly = 1) { wishlistUseCase.addToWishlist(2) }
            // 🌟 核心断言：验证移入心愿单的交互行为是否精确外传
            coVerify(exactly = 1) {
                telemetryManager.trackAction(
                    actionName = TelemetryEvents.Actions.BOOK_ADD_WISHLIST,
                    bookId = 2
                )
            }
        }

    @Test
    fun `removeFromWishlist should call use case and track action`() = runTest {
        // When
        viewModel.removeFromWishlist(bookId = 3)

        // Then
        coVerify(exactly = 1) { wishlistUseCase.removeFromWishlist(3) }
        // 🌟 核心断言：验证直接卡片滑除或点击移除时的埋点完备性
        coVerify(exactly = 1) {
            telemetryManager.trackAction(
                actionName = TelemetryEvents.Actions.BOOK_REMOVE_WISHLIST,
                bookId = 3
            )
        }
    }
}