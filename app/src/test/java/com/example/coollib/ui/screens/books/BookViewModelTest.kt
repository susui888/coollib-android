package com.example.coollib.ui.screens.books

import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.usecase.BookUseCase
import com.example.coollib.ui.previewSupport.MockBooks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    private lateinit var viewModel: BookViewModel
    private val bookUseCase: BookUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // 覆盖 Main Dispatcher
        Dispatchers.setMain(testDispatcher)
        viewModel = BookViewModel(bookUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchBooks_updatesBooksState() = runTest {
        val books = MockBooks.list
        val query = SearchQuery()

        coEvery { bookUseCase.searchBooks(query) } returns books

        viewModel.searchBooks(query)

        // 推进 coroutine 执行完成
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(books, viewModel.books.value)
    }

    @Test
    fun selectBook_updatesSelectedBookState() = runTest {
        val firstBook = MockBooks.list.first()

        coEvery { bookUseCase.getBookById(firstBook.id) } returns firstBook

        viewModel.selectBook(firstBook.id)

        // 推进 coroutine 执行完成
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(firstBook, viewModel.selectedBook.value)
    }
}