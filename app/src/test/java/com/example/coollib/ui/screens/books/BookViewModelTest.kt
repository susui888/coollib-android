package com.example.coollib.ui.screens.books

import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.usecase.BookUseCase
import com.example.coollib.domain.usecase.ReviewUseCase
import com.example.coollib.ui.previewSupport.MockBooks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    private val bookUseCase: BookUseCase = mockk()
    private val reviewUseCase: ReviewUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: BookViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { reviewUseCase.getReviewsByBook(any()) } returns emptyList()
        viewModel = BookViewModel(bookUseCase, reviewUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectBook should update selectedBook and fetch associated reviews`() = runTest {
        // Given
        val firstBook = MockBooks.list.first()
        val mockReviews = com.example.coollib.ui.previewSupport.MockReviews.list

        coEvery { bookUseCase.getBookById(firstBook.id) } returns firstBook
        // 覆盖 setup 中的默认值，返回具体的模拟数据
        coEvery { reviewUseCase.getReviewsByBook(firstBook.id) } returns mockReviews

        // When
        viewModel.selectBook(firstBook.id)

        // Then
        assertEquals(firstBook, viewModel.selectedBook.value)
        assertEquals(mockReviews, viewModel.reviews.value) // 验证评论是否成功加载并更新了 StateFlow
    }

    @Test
    fun `loadReviews should set reviews state to empty list when use case fails`() = runTest {
        // Given
        val bookId = 123
        // 模拟异常情况
        coEvery { reviewUseCase.getReviewsByBook(bookId) } returns emptyList()

        // When
        viewModel.selectBook(bookId)

        // Then
        assertEquals(0, viewModel.reviews.value.size)
    }
}
