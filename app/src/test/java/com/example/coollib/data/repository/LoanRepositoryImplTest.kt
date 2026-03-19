package com.example.coollib.data.repository

import android.util.Log
import com.example.coollib.data.remote.LoanApi
import com.example.coollib.data.remote.LoanDto
import com.example.coollib.domain.model.LoanStatus
import com.example.coollib.domain.repository.BookRepository
import com.example.coollib.ui.previewSupport.MockBooks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class LoanRepositoryImplTest {

    private lateinit var repository: LoanRepositoryImpl
    private val loanApi: LoanApi = mockk()
    private val bookRepository: BookRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // 模拟静态 Log 类
        mockkStatic(Log::class)
        // 允许 Log.e 被调用而不抛出异常
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        repository = LoanRepositoryImpl(
            loanApi = loanApi,
            bookRepository = bookRepository,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `getAllLoans returns mapped domain list on success`() = runTest {
        val book = MockBooks.list.first()
        val loanDto = LoanDto(
            id = 1,
            bookId = book.id,
            borrowDate = LocalDate.now(),
            dueDate = LocalDate.now().plusDays(14),
            status = LoanStatus.Borrowed
        )

        coEvery { loanApi.getAllLoans() } returns Response.success(listOf(loanDto))
        coEvery { bookRepository.getBookById(book.id) } returns book

        val result = repository.getAllLoans()

        assertEquals(1, result.size)
        assertEquals(book.title, result.first().book?.title)
    }

    @Test
    fun `getAllLoans returns empty list when API fails`() = runTest {
        val errorResponse = Response.error<List<LoanDto>>(404, "".toResponseBody(null))
        coEvery { loanApi.getAllLoans() } returns errorResponse

        val result = repository.getAllLoans()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllLoans returns empty list when exception occurs`() = runTest {
        coEvery { loanApi.getAllLoans() } throws RuntimeException("Network Error")

        val result = repository.getAllLoans()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllLoans returns loan with null book if book repository fails`() = runTest {
        val loanDto = LoanDto(
            id = 1,
            bookId = 999,
            borrowDate = LocalDate.now(),
            dueDate = LocalDate.now().plusDays(14),
            status = LoanStatus.Borrowed
        )
        coEvery { loanApi.getAllLoans() } returns Response.success(listOf(loanDto))
        coEvery { bookRepository.getBookById(999) } returns null

        val result = repository.getAllLoans()

        assertEquals(1, result.size)
        assertEquals(null, result.first().book)
    }
}