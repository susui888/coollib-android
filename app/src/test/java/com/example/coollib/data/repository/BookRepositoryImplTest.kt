package com.example.coollib.data.repository

import com.example.coollib.data.local.BookDao
import com.example.coollib.data.local.CategoryDao
import com.example.coollib.data.mapper.toDomain
import com.example.coollib.data.mapper.toDto
import com.example.coollib.data.mapper.toEntity
import com.example.coollib.data.remote.BookApi
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockCategory
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
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

    private val categoryDao: CategoryDao = mockk()

    @Before
    fun setup() {
        repository = BookRepositoryImpl(api, dao, categoryDao = categoryDao)
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
    fun `getNewestBooks returns mapped domain list when cache is empty`() = runTest {
        // Arrange
        // 注意：getCachedNewestBooks 返回 Flow，属于普通调用，用 every
        every { dao.getCachedNewestBooks() } returns flowOf(emptyList())

        // updateNewestCache 是 suspend，用 coEvery
        coEvery { dao.updateNewestCache(any(), any()) } just Runs

        val mockDtoList = MockBooks.list.map { it.toEntity().toDto() }
        coEvery { api.getNewestBooks() } returns Response.success(mockDtoList)

        // Act
        val result = repository.getNewestBooks()

        // Assert
        assertEquals(MockBooks.list.size, result.size)
        assertEquals(MockBooks.list.first().title, result.first().title)

        // 验证交互顺序
        coVerifyOrder {
            dao.getCachedNewestBooks()
            api.getNewestBooks()
            dao.updateNewestCache(any(), any())
        }
    }

    @Test
    fun `getCategory returns remote data when local is empty`() = runTest {
        // 1. 模拟数据库为空：发射一个空列表
        coEvery { categoryDao.getAllCategory() } returns flowOf(emptyList())

        // 2. 模拟网络返回数据
        val mockDtoList = MockCategory.list.map { it.toDto() }
        coEvery { api.getCategory() } returns Response.success(mockDtoList)

        // 3. 模拟存入数据库的操作（避免 verify 时报错）
        coEvery { categoryDao.insertAll(any()) } returns Unit

        val result = repository.getCategory()

        // 验证结果
        assertEquals(MockCategory.list.size, result.size)
        // 验证是否触发了网络请求
        coVerify(exactly = 1) { api.getCategory() }
        // 验证是否存入了数据库
        coVerify { categoryDao.insertAll(any()) }
    }
}