package com.example.coollib.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartEntity)

    @Query("SELECT * FROM cart ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM cart WHERE id = :id)")
    fun isBookInCart(id: Int): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM cart")
    fun getCount(): Flow<Int>

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun removeFromCart(id: Int)

    @Query("DELETE FROM cart")
    suspend fun deleteAll()
}