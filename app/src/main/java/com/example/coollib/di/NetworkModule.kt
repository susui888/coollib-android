package com.example.coollib.di

import android.content.Context
import com.example.coollib.data.local.SessionManager
import com.example.coollib.data.remote.APIConfig
import com.example.coollib.data.remote.AuthInterceptor
import com.example.coollib.data.remote.BookApi
import com.example.coollib.data.remote.CartApi
import com.example.coollib.data.remote.InstantAdapter
import com.example.coollib.data.remote.LoanApi
import com.example.coollib.data.remote.LocalDateAdapter
import com.example.coollib.data.remote.ReviewApi
import com.example.coollib.data.remote.UserApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionManager: SessionManager
    ): AuthInterceptor {
        return AuthInterceptor(sessionManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(LocalDateAdapter())
            .add(InstantAdapter())      // Add the InstantAdapter for review
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("${APIConfig.SERVER}/api/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory.create(moshi).asLenient()
            )
            .build()
    }

    @Provides
    fun provideBookApi(
        retrofit: Retrofit
    ): BookApi {
        return retrofit.create(BookApi::class.java)
    }

    @Provides
    fun provideUserApi(
        retrofit: Retrofit
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    fun provideCartApi(
        retrofit: Retrofit
    ): CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Provides
    fun provideLoanApi(
        retrofit: Retrofit
    ): LoanApi {
        return retrofit.create(LoanApi::class.java)
    }

    @Provides
    fun provideReviewApi(
        retrofit: Retrofit
    ): ReviewApi {
        return retrofit.create(ReviewApi::class.java)
    }
}