package com.example.coollib.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_elementsAreDisplayed() {
        composeTestRule.setContent {
            RegisterScreenContent(
                username = "",
                onUsernameChange = {},
                password = "",
                onPasswordChange = {},
                email = "",
                onEmailChange = {},
                onRegisterClick = {}
            )
        }

        // 验证 Logo、标题、输入框标签和注册按钮是否显示
        composeTestRule.onNodeWithContentDescription("CoolLib Logo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create an account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
    }

    @Test
    fun registerScreen_showsErrorMessage_whenErrorExists() {
        val testErrorMessage = "Username already exists"
        composeTestRule.setContent {
            RegisterScreenContent(
                username = "testuser",
                onUsernameChange = {},
                password = "password123",
                onPasswordChange = {},
                email = "test@example.com",
                onEmailChange = {},
                onRegisterClick = {},
                registerError = testErrorMessage
            )
        }

        // 验证错误提示信息是否显示
        composeTestRule.onNodeWithText(testErrorMessage).assertIsDisplayed()
    }

    @Test
    fun registerScreen_inputTextFieldsWork() {
        composeTestRule.setContent {
            var username by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            RegisterScreenContent(
                username = username,
                onUsernameChange = { username = it },
                password = "",
                onPasswordChange = {},
                email = email,
                onEmailChange = { email = it },
                onRegisterClick = {}
            )
        }

        // 模拟输入用户名和邮箱
        composeTestRule.onNodeWithText("Username").performTextInput("newuser")
        composeTestRule.onNodeWithText("Email").performTextInput("new@example.com")
        
        // 验证输入的内容是否存在于 UI 中
        composeTestRule.onNodeWithText("newuser").assertExists()
        composeTestRule.onNodeWithText("new@example.com").assertExists()
    }
}
