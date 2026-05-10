package com.example.coollib.ui.screens.auth

import android.util.Patterns.EMAIL_ADDRESS
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coollib.R

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    onRegistered: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onLoginSuccess: (String, String) -> Unit = { _, _ -> },
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val registerResult by viewModel.registerResult.collectAsState()
    val loginResult by viewModel.loginResult.collectAsState()

    val registerError = registerResult?.exceptionOrNull()?.message

    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$")

    val isUsernameValid = username.length >= 3
    val isPasswordValid = password.length >= 6
    val isEmailValid = EMAIL_ADDRESS.matcher(email).matches()
    val canRegister = isUsernameValid && isPasswordValid && isEmailValid

    RegisterScreenContent(
        username = username,
        onUsernameChange = { username = it },
        password = password,
        onPasswordChange = { password = it },
        email = email,
        onEmailChange = { email = it },
        onRegisterClick = {
            if (canRegister) {
                viewModel.register(username, password, email)
            }
        },
        registerError = registerError,
        isUsernameError = !isUsernameValid && username.isNotEmpty(),
        isPasswordError = !isPasswordValid && password.isNotEmpty(),
        isEmailError = !isEmailValid && email.isNotEmpty(),
        canRegister = canRegister
    )
    LaunchedEffect(registerResult) {
        registerResult?.onSuccess { message ->
            if (message.isNotBlank()) {
                onRegistered("User registered successfully.")
            }
        }
    }

    LaunchedEffect(loginResult) {
        loginResult?.onSuccess { (token, username) ->
            onLoginSuccess(token, username)

            viewModel.clearRegisterResult()
            viewModel.clearLoginResult()
        }
    }
}

@Composable
fun RegisterScreenContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    registerError: String? = null,
    isUsernameError: Boolean = false,
    isPasswordError: Boolean = false,
    isEmailError: Boolean = false,
    canRegister: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.books2),
            contentDescription = "CoolLib Logo",
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Create an account",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Input Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isUsernameError,
                    supportingText = {
                        if (isUsernameError){
                            Text("Must be more than 2 characters")
                        }
                    }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = isPasswordError,
                    supportingText = {
                        if (isPasswordError){
                            Text("Must be more than 5 characters")
                        }
                    }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isEmailError,
                    supportingText = {
                        if (isEmailError){
                            Text("Invalid email format")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRegisterClick,
            enabled = canRegister,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Register")
        }

        registerError?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    var username by remember { mutableStateOf("johndoe") }
    var password by remember { mutableStateOf("password123") }
    var email by remember { mutableStateOf("john@example.com") }
    var error by remember { mutableStateOf("Username already exists") }

    RegisterScreenContent(
        username = username,
        onUsernameChange = { username = it },
        password = password,
        onPasswordChange = { password = it },
        email = email,
        onEmailChange = { email = it },
        onRegisterClick = { },
        registerError = error
    )
}