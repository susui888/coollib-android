package com.example.coollib.ui.screens.auth

import android.util.Log
import com.example.coollib.data.local.SessionManager
import com.example.coollib.data.remote.MessageResponse
import com.example.coollib.domain.usecase.UserUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val userUseCase: UserUseCase = mockk()
    private val sessionManager: SessionManager = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // ✅ 关键：Mock 静态 Log 类，防止在单元测试中崩溃
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        // 默认 mock sessionManager 的行为
        every { sessionManager.saveToken(any(), any()) } returns Unit

        viewModel = UserViewModel(userUseCase, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        // ✅ 取消静态 mock，避免影响其他测试类
        unmockkStatic(Log::class)
    }

    @Test
    fun `login success should save token and update loginResult`() = runTest {
        // Given
        val username = "testUser"
        val password = "password"
        val token = "mock_token"
        val mockResponse = mapOf("token" to token, "username" to username)

        coEvery { userUseCase.login(username, password) } returns mockResponse

        // When
        viewModel.login(username, password)

        // Then
        verify { sessionManager.saveToken(token, username) }

        val result = viewModel.loginResult.value
        assertTrue("Expected success result", result?.isSuccess == true)
        assertEquals(token to username, result?.getOrNull())
    }


    @Test
    fun `register success should update registerResult`() = runTest {
        // 1. 准备数据
        val username = "newUser"
        val password = "password"
        val email = "test@example.com"
        val msgText = "User registered successfully"
        val mockResponse = MessageResponse(message = msgText)

        // 修改这里：构造一个 Map 而不是纯 String
        val mockLoginResponse = mapOf(
            "token" to "mock_token",
            "username" to "newUser"
        )

        // 2. Mock 注册接口
        coEvery { userUseCase.register(username, password, email) } returns mockResponse

        // 3. Mock 自动登录接口，返回 Map 类型
        coEvery { userUseCase.login(username, password) } returns mockLoginResponse

        // 4. 执行
        viewModel.register(username, password, email)

        // 5. 等待协程执行完毕
        advanceUntilIdle()

        // 6. 断言
        val result = viewModel.registerResult.value

        assertNotNull("Result should not be null", result)
        assertTrue("Expected success result", result!!.isSuccess)
        assertEquals(msgText, result.getOrNull())
    }

    @Test
    fun `clearLoginResult should set result to null`() = runTest {
        // Given
        coEvery { userUseCase.login(any(), any()) } returns mapOf("token" to "t", "username" to "u")
        viewModel.login("u", "p")

        // When
        viewModel.clearLoginResult()

        // Then
        assertEquals(null, viewModel.loginResult.value)
    }
}