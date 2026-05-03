package com.example.coollib.data.remote

import com.example.coollib.data.local.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private val sessionManager: SessionManager = mockk()
    private lateinit var authInterceptor: AuthInterceptor
    private val chain: Interceptor.Chain = mockk()
    private val request: Request = Request.Builder().url("http://test.com").build()
    private val response: Response = mockk()

    @Before
    fun setup() {
        authInterceptor = AuthInterceptor(sessionManager)
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response
    }

    @Test
    fun `intercept should add Authorization header when token exists and host is internal`() {
        // Given
        val token = "mock_token"
        val internalUrl = "https://api.ryansu.uk/v1/books".toHttpUrl()

        val request = Request.Builder().url(internalUrl).build()
        every { chain.request() } returns request
        every { sessionManager.getToken() } returns token
        every { chain.proceed(any()) } returns mockk()

        // When
        authInterceptor.intercept(chain)

        // Then
        verify {
            chain.proceed(withArg {
                assertEquals("Bearer $token", it.header("Authorization"))
            })
        }
    }

    @Test
    fun `intercept should not add Authorization header when token is null`() {
        // Given
        every { sessionManager.getToken() } returns null

        // When
        authInterceptor.intercept(chain)

        // Then
        verify {
            chain.proceed(withArg {
                assertEquals(null, it.header("Authorization"))
            })
        }
    }

    @Test
    fun `intercept should not add Authorization header when token is empty`() {
        // Given
        every { sessionManager.getToken() } returns ""

        // When
        authInterceptor.intercept(chain)

        // Then
        verify {
            chain.proceed(withArg {
                assertEquals(null, it.header("Authorization"))
            })
        }
    }
}
