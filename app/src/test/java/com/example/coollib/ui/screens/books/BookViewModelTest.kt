package com.example.coollib.ui.screens.books

import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.usecase.BookUseCase
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
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: BookViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BookViewModel(bookUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchBooks should update books state with results from use case`() = runTest {
        // Given
        val books = MockBooks.list
        val query = SearchQuery()
        coEvery { bookUseCase.searchBooks(query) } returns books

        // When
        viewModel.searchBooks(query)

        // Then
        assertEquals(books, viewModel.books.value)
    }

    @Test
    fun `selectBook should update selectedBook state with result from use case`() = runTest {
        // Given
        val firstBook = MockBooks.list.first()
        coEvery { bookUseCase.getBookById(firstBook.id) } returns firstBook

        // When
        viewModel.selectBook(firstBook.id)

        // Then
        assertEquals(firstBook, viewModel.selectedBook.value)
    }
}
