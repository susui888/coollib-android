package com.example.coollib.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.coollib.ui.mapper.toUiModel
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockCart
import com.example.coollib.ui.previewSupport.MockCategory
import com.example.coollib.ui.previewSupport.MockWishlist
import com.example.coollib.ui.screens.home.HomeScreen
import com.example.coollib.ui.screens.books.BookScreen
import com.example.coollib.ui.screens.books.BookViewModel
import com.example.coollib.ui.screens.books.SearchScreen
import com.example.coollib.ui.screens.checkout.CartScreen
import com.example.coollib.ui.screens.statistics.StatisticsScreen


/*
    NavController
        → 控制页面跳转
        → 例如：navController.navigate("books")

    NavHost
        → 管理所有页面
        → 指定 startDestination（默认页面）

    route
        → 每个页面的唯一地址
        → 例如："home" / "books"

    composable
        → 注册一个 Compose 页面
        → composable("home") { HomeScreen() }
*/

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                categoryList = MockCategory.list,
                lastViewBooks = MockBooks.list.map { it.toUiModel() }.shuffled(),
                wishlist = MockWishlist.list.map { it.toUiModel() }.shuffled(),
                newestBooks = MockBooks.list.map { it.toUiModel() }.shuffled(),
                onCategoryClick = {},
                onBookClick = {})
        }

        composable(Screen.Books.route) {
            BookScreen()
        }

        composable(Screen.Cart.route) {
            CartScreen(
                cartItems = MockCart.list,
                wishlistItems = MockWishlist.list,
                isBorrowing = false,
                onBookClick = {},
                onRemoveCartItem = {},
                onRemoveWishlistItem = {},
                onBorrow = {}
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }

        composable(Screen.Search.route) {
            SearchScreen(
                history = emptyList(),
                onBack = {},
                onClearAll = {},
                onDeleteItem = {},
                onSearch = {}
            )
        }
    }
}