package com.example.coollib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.coollib.data.local.SessionManager
import com.example.coollib.ui.components.BottomBar
import com.example.coollib.ui.components.TopBar
import com.example.coollib.ui.navigation.AppNavGraph
import com.example.coollib.ui.navigation.Screen
import com.example.coollib.ui.screens.checkout.CartViewModel
import com.example.coollib.ui.theme.CoolLibTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoolLibTheme {
                MainScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val cartCount by cartViewModel.cartCount.collectAsStateWithLifecycle()

    val sessionManager = remember { SessionManager(context) }
    var token by remember { mutableStateOf(sessionManager.getToken()) }
    var username by remember { mutableStateOf(sessionManager.getUsername()) }

    val isLoggedIn = !token.isNullOrEmpty()

    Scaffold(
        modifier = Modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        ),

        topBar = {
            TopBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
                isLoggedIn = isLoggedIn,
                userName = username,
                onLogout = {
                    sessionManager.clearSession()
                    token = null
                    username = null
                },
                onScanClick = {
                    navController.navigate(Screen.Scanner.route)
                }
            )
        },

        bottomBar = {
            BottomBar(navController,cartCount)
        }

    ) { padding ->

        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding),
            onLoginSuccess = { newToken, newUsername ->
                token = newToken
                username = newUsername
            }
        )
    }
}