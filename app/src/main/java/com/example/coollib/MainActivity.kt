package com.example.coollib

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.coollib.domain.model.Book
import com.example.coollib.ui.navigation.AppNavGraph
import com.example.coollib.ui.navigation.BottomBar
import com.example.coollib.ui.navigation.TopBar
import com.example.coollib.ui.screens.books.BookScreen
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
fun MainScreen() {

    val context = LocalContext.current
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        ),

        topBar = {
            TopBar(scrollBehavior,
                onScanClick = {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    context.startActivity(intent)
                })
        },

        bottomBar = {
            BottomBar(navController)
        }

    ) { padding ->

        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}