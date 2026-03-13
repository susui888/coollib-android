package com.example.coollib.data.repository

import com.example.coollib.data.local.BookDao
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toDto
import com.example.coollib.data.mapper.toEntity
import com.example.coollib.data.remote.BookApi
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryImplTest {

    private lateinit var repository: BookRepositoryImpl
    private val api: BookApi = mockk()
    private val dao: BookDao = mockk()

    @Before
    fun setup() {
        repository = BookRepositoryImpl(api, dao)
    }

    @Test
    fun `searchBooks returns mapped domain list using MockBooks`() = runTest {
        val query = SearchQuery(
            category = null,
            author = null,
            publisher = null,
            year = null,
            searchTerm = "Test"
        )

        val mockDtoList = MockBooks.list.map { it.toEntity().toDto() }
        coEvery { api.searchBooks(any(), any(), any(), any(), any()) } returns Response.success(mockDtoList)

        val result = repository.searchBooks(query)

        assertEquals(MockBooks.list.size, result.size)
        assertEquals(MockBooks.list.first().title, result.first().title)
        coVerify { api.searchBooks(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `getBookById returns local book if exists`() = runTest {
        val mockBook = MockBooks.list.first()
        val entity = mockBook.toEntity()
        coEvery { dao.getBookById(mockBook.id) } returns entity
        coEvery { dao.updateBook(any()) } returns Unit

        val result = repository.getBookById(mockBook.id)

        assertEquals(mockBook.title, result?.title)
        coVerify { dao.getBookById(mockBook.id) }
        coVerify { dao.updateBook(any()) }
    }

    @Test
    fun `getBookById fetches from API if local not found`() = runTest {
        val mockBook = MockBooks.list.first()
        coEvery { dao.getBookById(mockBook.id) } returns null
        coEvery { api.getBookById(mockBook.id) } returns Response.success(mockBook.toEntity().toDto())
        coEvery { dao.insertBook(any()) } returns Unit

        val result = repository.getBookById(mockBook.id)

        assertEquals(mockBook.title, result?.title)
        coVerify { api.getBookById(mockBook.id) }
        coVerify { dao.insertBook(any()) }
    }

    @Test
    fun `getBooks returns flow mapped from DAO`() = runTest {
        coEvery { dao.getBooks(10) } returns flowOf(MockBooks.list.map { it.toEntity() })

        val result = repository.getBooks(10).first()

        assertEquals(MockBooks.list.size, result.size)
        assertEquals(MockBooks.list.first().title, result.first().title)
        coVerify { dao.getBooks(10) }
    }

    @Test
    fun `getNewestBooks returns mapped domain list`() = runTest {
        coEvery { api.getNewestBooks() } returns Response.success(MockBooks.list.map { it.toEntity().toDto() })

        val result = repository.getNewestBooks()

        assertEquals(MockBooks.list.size, result.size)
        assertEquals(MockBooks.list.first().title, result.first().title)
        coVerify { api.getNewestBooks() }
    }

    @Test
    fun `getCategory returns mapped domain list`() = runTest {
        coEvery { api.getCategory() } returns Response.success(MockCategory.list.map { it.toDto() })

        val result = repository.getCategory()

        assertEquals(MockCategory.list.size, result.size)
        assertEquals(MockCategory.list.first().name, result.first().name)
        coVerify { api.getCategory() }
    }
}