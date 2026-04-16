package com.example.coollib.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM book ORDER BY updatedAt DESC LIMIT :limit")
    fun getBooks(limit: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM book WHERE id = :id")
    suspend fun getBookById(id: Int): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewestRefs(refs: List<NewestBookRef>)

    @Query("DELETE FROM newest_book_refs")
    suspend fun clearNewestRefs()

    @Transaction
    suspend fun updateNewestCache(books: List<BookEntity>, refs: List<NewestBookRef>) {
        books.forEach { insertBook(it) }

        clearNewestRefs()

        insertNewestRefs(refs)
    }

    @Transaction
    @Query(
        """
    SELECT * FROM book 
    INNER JOIN newest_book_refs ON book.id = newest_book_refs.bookId 
    ORDER BY newest_book_refs.priority ASC
"""
    )
    fun getCachedNewestBooks(): Flow<List<BookEntity>>
}