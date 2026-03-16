package com.example.coollib.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.coollib.R

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: UserViewModel = hiltViewModel(),
    onLoggedIn: (token: String, username: String) -> Unit,
    onRegisterClick: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val registerMsg = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("msg")

    LaunchedEffect(registerMsg) {
        registerMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("msg")
        }
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }

    val loginResult by viewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        loginResult?.fold(
            onSuccess = { (token, username) ->
                onLoggedIn(token, username)
                viewModel.clearLoginResult()
            },
            onFailure = { e ->
                snackbarHostState.showSnackbar(e.message ?: "Login failed")
                viewModel.clearLoginResult()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SnackbarHost(hostState = snackbarHostState)

        LoginScreenContent(
            username = username,
            onUsernameChange = { username = it },
            password = password,
            onPasswordChange = { password = it },
            onLoginClick = {
                when {
                    username.length < 5 -> loginError = "Username must be longer than 5 characters"
                    password.length < 6 -> loginError = "Password must be longer than 6 characters"
                    else -> {
                        loginError = null
                        viewModel.login(username, password)
                    }
                }
            },
            onRegisterClick = onRegisterClick,
            loginError = loginError,
            onClose = onClose
        )
    }
}

@Composable
fun LoginScreenContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    loginError: String? = null,
    onClose: () -> Unit = {}
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

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to CoolLib",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Login")
        }

        loginError?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = Color.Red)
        }

        // Register link
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Don't have an account? Register",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onRegisterClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    var username by remember { mutableStateOf("john_doe") }
    var password by remember { mutableStateOf("password123") }
    var error by remember { mutableStateOf("Invalid credentials") }

    LoginScreenContent(
        username = username,
        onUsernameChange = { username = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginClick = {},
        onRegisterClick = { },
        loginError = error
    )
}