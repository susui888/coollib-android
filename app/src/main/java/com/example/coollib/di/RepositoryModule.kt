package com.example.coollib.di


import com.example.coollib.data.repository.BookRepositoryImpl
import com.example.coollib.data.repository.CartRepositoryImpl
import com.example.coollib.domain.repository.BookRepository
import com.example.coollib.domain.repository.CartRepository
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
}