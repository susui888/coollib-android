package com.example.coollib.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_elementsAreDisplayed() {
        composeTestRule.setContent {
            LoginScreenContent(
                username = "",
                onUsernameChange = {},
                password = "",
                onPasswordChange = {},
                onLoginClick = {},
                onRegisterClick = {}
            )
        }

        // 验证 Logo、标题和按钮是否显示
        composeTestRule.onNodeWithContentDescription("CoolLib Logo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Welcome to CoolLib").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Don't have an account? Register").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsErrorMessage_whenInputIsInvalid() {
        composeTestRule.setContent {
            LoginScreenContent(
                username = "abc", 
                onUsernameChange = {},
                password = "123", 
                onPasswordChange = {},
                onLoginClick = { },
                onRegisterClick = {},
                loginError = "Username must be longer than 5 characters"
            )
        }

        // 验证错误信息是否显示
        composeTestRule.onNodeWithText("Username must be longer than 5 characters").assertIsDisplayed()
    }

    @Test
    fun loginScreen_inputTextFieldsWork() {
        composeTestRule.setContent {
            var username by remember { mutableStateOf("") }
            LoginScreenContent(
                username = username,
                onUsernameChange = { username = it },
                password = "",
                onPasswordChange = {},
                onLoginClick = {},
                onRegisterClick = {}
            )
        }

        // 模拟输入用户名
        val usernameField = composeTestRule.onNodeWithText("Username")
        usernameField.performTextInput("testuser")
        
        // 验证输入内容是否出现在屏幕上
        composeTestRule.onNodeWithText("testuser").assertExists()
    }
}
