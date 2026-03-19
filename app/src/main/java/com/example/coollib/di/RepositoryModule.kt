package com.example.coollib.di


import com.example.coollib.data.repository.BookRepositoryImpl
import com.example.coollib.data.repository.CartRepositoryImpl
import com.example.coollib.data.repository.LoanRepositoryImpl
import com.example.coollib.data.repository.SearchHistoryRepositoryImpl
import com.example.coollib.data.repository.UserRepositoryImpl
import com.example.coollib.data.repository.WishlistRepositoryImpl
import com.example.coollib.domain.repository.BookRepository
import com.example.coollib.domain.repository.CartRepository
import com.example.coollib.domain.repository.LoanRepository
import com.example.coollib.domain.repository.SearchHistoryRepository
import com.example.coollib.domain.repository.UserRepository
import com.example.coollib.domain.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBookRepository(
        impl: BookRepositoryImpl
    ): BookRepository

    @Binds
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun bindWishlistRepository(
        impl: WishlistRepositoryImpl
    ): WishlistRepository

    @Binds
    abstract fun bingSearchHistoryRepository(
        impl: SearchHistoryRepositoryImpl
    ): SearchHistoryRepository

    @Binds
    abstract fun bingUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bingLoanRepository(
        impl: LoanRepositoryImpl
    ): LoanRepository
}