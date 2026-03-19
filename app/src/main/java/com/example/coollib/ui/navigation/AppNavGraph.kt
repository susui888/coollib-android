package com.example.coollib.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.ui.screens.auth.LoginScreen
import com.example.coollib.ui.screens.auth.RegisterScreen
import com.example.coollib.ui.screens.scan.ScannerScreen
import com.example.coollib.ui.screens.books.SearchScreen
import com.example.coollib.ui.screens.checkout.CartScreen
import com.example.coollib.ui.screens.home.HomeScreen
import com.example.coollib.ui.screens.statistics.LoanScreen
import com.example.coollib.ui.screens.statistics.StatisticsScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLoginSuccess: (token: String, username: String) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                modifier = Modifier.testTag("HomeScreen"),
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.Books.createRoute(SearchQuery(category = categoryId)))
                },
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                }
            )
        }


        composable(Screen.Cart.route) {
            CartScreen(
                modifier = Modifier.testTag("CartScreen"),
                onBack = { navController.popBackStack() },
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onLogin = { navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                modifier = Modifier.testTag("StatisticsScreen"),
                onSeeMoreLoans = {
                    navController.navigate(Screen.Loan.route)
                }
            )
        }
        
        composable(Screen.Loan.route) {
            LoanScreen(
                onBack = { navController.popBackStack() },
                onLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                modifier = Modifier.testTag("SearchScreen"),
                onBack = {
                    navController.popBackStack()
                },
                onSearch = { searchTerm ->
                    navController.navigate(Screen.Books.createRoute(SearchQuery(searchTerm = searchTerm)))
                }
            )
        }

        booksRoute(navController)

        bookDetailRoute(navController)

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoggedIn = { token, username ->
                    onLoginSuccess(token, username)
                    navController.popBackStack()
                             },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onClose = { navController.popBackStack() }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistered = { msg ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("msg", msg)
                    navController.navigate(Screen.Home.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Scanner.route) {
            ScannerScreen(
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route) {
                        popUpTo(Screen.Scanner.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}