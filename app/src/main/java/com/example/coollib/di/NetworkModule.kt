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
import com.example.coollib.data.remote.TelemetryApi
import com.example.coollib.data.remote.TelemetryInterceptor
import com.example.coollib.data.remote.UserApi
import com.example.coollib.data.repository.TelemetryRepositoryImpl
import com.example.coollib.domain.repository.TelemetryRepository
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
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

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
    fun provideTelemetryInterceptor(): TelemetryInterceptor {
        return TelemetryInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        telemetryInterceptor: TelemetryInterceptor
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(telemetryInterceptor)
            .addInterceptor(authInterceptor)

        if (com.example.coollib.BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
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

    // =================================================================
    // 🛠️ Retrofit Instance Routing Configurations
    // =================================================================

    /**
     * 1. Core Business Retrofit Instance
     */
    @Provides
    @Singleton
    @Named("BusinessRetrofit")
    fun provideBusinessRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${APIConfig.SERVER}/api/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    /**
     * 2. Telemetry-Specific Retrofit Instance (Points to independent teleMetryURL)
     */
    @Provides
    @Singleton
    @Named("TelemetryRetrofit")
    fun provideTelemetryRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${APIConfig.TELEMETRY_URL}/api/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    // =================================================================
    // Telemetry API Instantiation (Bound to TelemetryRetrofit)
    // =================================================================

    @Provides
    @Singleton
    fun provideTelemetryApi(
        @Named("TelemetryRetrofit") retrofit: Retrofit
    ): TelemetryApi {
        return retrofit.create(TelemetryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTelemetryRepository(
        telemetryApi: TelemetryApi
    ): TelemetryRepository {
        return TelemetryRepositoryImpl(telemetryApi)
    }


    // =================================================================
    // Core Business API Instantiation (Explicitly bound to BusinessRetrofit)
    // =================================================================

    @Provides
    fun provideBookApi(@Named("BusinessRetrofit") retrofit: Retrofit): BookApi =
        retrofit.create(BookApi::class.java)

    @Provides
    fun provideUserApi(@Named("BusinessRetrofit") retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    fun provideCartApi(@Named("BusinessRetrofit") retrofit: Retrofit): CartApi =
        retrofit.create(CartApi::class.java)

    @Provides
    fun provideLoanApi(@Named("BusinessRetrofit") retrofit: Retrofit): LoanApi =
        retrofit.create(LoanApi::class.java)

    @Provides
    fun provideReviewApi(@Named("BusinessRetrofit") retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)
}