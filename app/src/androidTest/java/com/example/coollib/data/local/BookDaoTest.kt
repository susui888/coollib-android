package com.example.coollib.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.coollib.data.mapper.toEntity
import com.example.coollib.ui.previewSupport.MockBooks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var bookDao: BookDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        bookDao = db.bookDao()

        // 插入 MockBooks 数据
        runTest {
            MockBooks.list.forEach { book ->
                bookDao.insertBook(book.toEntity())
            }
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getBooks_returnsCorrectList() = runTest {
        val books = bookDao.getBooks(100).first()

        assertEquals(MockBooks.list.size, books.size)
    }

    @Test
    fun getBookById_returnsCorrectBook() = runTest {
        val targetBook = MockBooks.list.first()
        val loadedBook = bookDao.getBookById(targetBook.id)

        assertEquals(targetBook.id, loadedBook?.id)
        assertEquals(targetBook.title, loadedBook?.title)
    }
}