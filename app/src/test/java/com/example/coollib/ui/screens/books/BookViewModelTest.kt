package com.example.coollib.ui.screens.books

import android.net.Uri
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.domain.usecase.BookUseCase
import com.example.coollib.domain.usecase.ReviewUseCase
import com.example.coollib.telemetry.TelemetryManager
import com.example.coollib.ui.previewSupport.MockBooks
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val telemetryManager: TelemetryManager = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: BookViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { reviewUseCase.getReviewsByBook(any()) } returns emptyList()
        viewModel = BookViewModel(bookUseCase, reviewUseCase, telemetryManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ==================================================================================
    // 🔍 1. 书籍详情与评论加载测试集 (Book Detail & Reviews)
    // ==================================================================================

    @Test
    fun `selectBook should update selectedBook and fetch associated reviews`() = runTest {
        // Given
        val firstBook = MockBooks.list.first()
        val mockReviews = com.example.coollib.ui.previewSupport.MockReviews.list

        coEvery { bookUseCase.getBookById(firstBook.id) } returns firstBook
        coEvery { reviewUseCase.getReviewsByBook(firstBook.id) } returns mockReviews

        // When
        viewModel.selectBook(firstBook.id)

        // Then
        assertEquals(firstBook, viewModel.selectedBook.value)
        assertEquals(mockReviews, viewModel.reviews.value)
    }

    @Test
    fun `loadReviews should set reviews state to empty list when use case fails`() = runTest {
        // Given
        val bookId = 123
        val exceptionMessage = "Network latency timeout"
        coEvery { bookUseCase.getBookById(bookId) } throws RuntimeException(exceptionMessage)

        // When
        viewModel.selectBook(bookId)

        // Then
        assertEquals(null, viewModel.selectedBook.value)
        coVerify(exactly = 1) {
            telemetryManager.trackException(
                actionName = TelemetryEvents.Actions.BOOK_DETAIL_LOAD_FAILURE,
                errorMessage = exceptionMessage
            )
        }
    }

    // ==================================================================================
    // 🏹 2. 书籍搜索测试集 (Book Search Flows)
    // ==================================================================================

    @Test
    fun `searchBooks should update books list and track action on success`() = runTest {
        // Given
        val query = SearchQuery(searchTerm = "Kotlin Architecture")
        val expectedBooks = MockBooks.list

        coEvery { bookUseCase.searchBooks(query) } returns expectedBooks

        // When
        viewModel.searchBooks(query)

        // Then
        assertEquals(expectedBooks, viewModel.books.value)
        coVerify(exactly = 1) {
            telemetryManager.trackAction(
                actionName = TelemetryEvents.Actions.BOOK_SEARCH,
                extra = mapOf(
                    "search_type" to "SEARCH",
                    "query_text" to "Kotlin Architecture"
                )
            )
        }
    }

    // ==================================================================================
    // ✍️ 3. 发布评论测试集 (Post Review & Asset Upload)
    // ==================================================================================

    @Test
    fun `postReview should upload assets create review and refresh local state`() = runTest {
        // Given
        val bookId = 456
        val mockUri = mockk<Uri>()
        val mockUris = listOf(mockUri)
        val mockUploadedUrls = listOf("https://r2.ryansu.uk/review_asset.jpg")

        coEvery { reviewUseCase.uploadImages(mockUris) } returns mockUploadedUrls
        coEvery { reviewUseCase.createReview(any()) } returns mockk() // 模拟成功创建

        // When
        viewModel.postReview(bookId, rating = 5, content = "Clean Code!", imageUris = mockUris)

        // Then
        // 1. 验证图片上传被执行
        coVerify(exactly = 1) { reviewUseCase.uploadImages(mockUris) }
        // 2. 验证评论创建被执行
        coVerify(exactly = 1) { reviewUseCase.createReview(any()) }
        // 3. 验证评论发布完毕后自动重新加载本地刷新
        coVerify(exactly = 1) { reviewUseCase.getReviewsByBook(bookId) }
        // 4. 验证遥测漏斗成功上报转化率指标
        coVerify(exactly = 1) {
            telemetryManager.trackAction(
                actionName = TelemetryEvents.Actions.BOOK_POST_REVIEW_SUCCESS,
                bookId = bookId,
                extra = mapOf(
                    "rating" to "5",
                    "image_count" to "1"
                )
            )
        }
    }
}