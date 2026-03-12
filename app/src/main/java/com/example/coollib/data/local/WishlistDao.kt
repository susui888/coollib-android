package com.example.coollib.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Query("SELECT * FROM wishlist ORDER BY addedAt DESC")
    fun getAllWishlist(): Flow<List<WishlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE id = :id)")
    fun isBookInWishlist(id: Int): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM wishlist")
    fun getCount(): Flow<Int>

    @Query("DELETE FROM wishlist WHERE id = :id")
    suspend fun removeFromWishlist(id: Int)

    @Query("DELETE FROM wishlist")
    suspend fun deleteAll()
}