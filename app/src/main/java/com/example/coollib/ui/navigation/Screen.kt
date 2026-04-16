package com.example.coollib.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.RoundaboutRight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coollib.R
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.model.SearchType

sealed class Screen(
    val route: String,
    @param:StringRes val labelResourceId: Int,
    val icon: ImageVector
) {
    data object Home: Screen("home", R.string.home_label, Icons.Rounded.Home)

    data object Books : Screen("books/{type}/{value}", R.string.books_label, Icons.Rounded.Book) {
        fun createRoute(query: SearchQuery): String {
            return when (query.searchType) {
                SearchType.CATEGORY -> "books/category/${query.category}"
                SearchType.AUTHOR -> "books/author/${query.author}"
                SearchType.PUBLISHER -> "books/publisher/${query.publisher}"
                SearchType.YEAR -> "books/year/${query.year}"
                SearchType.SEARCH -> "books/search/${query.searchTerm}"
                SearchType.ALL -> "books/all/0"
            }
        }
    }

    data object Cart: Screen("cart", R.string.cart_label, Icons.Rounded.ShoppingBasket)
    data object Statistics: Screen("statistics",R.string.statistics_label,Icons.Rounded.Analytics)
    data object Search: Screen("search", R.string.search_label,Icons.Rounded.Search)
    
    data object Loan: Screen("loan/{filter}", R.string.loan_history_label, Icons.Rounded.History) {
        fun createRoute(filter: String) = "loan/$filter"
    }

    //Nested screen
    data object BookDetail: Screen("bookDetail/{id}", 0, Icons.Rounded.Book){
        fun createRoute(id: Int) = "bookDetail/$id"
    }

    // Auth screens
    data object Login: Screen("login", 0, Icons.AutoMirrored.Rounded.Login)
    data object Register: Screen("register", 0, Icons.Rounded.PersonAdd)
    
    data object Scanner: Screen("scanner", 0, Icons.Rounded.QrCodeScanner)

    data object About: Screen("about",0, Icons.Rounded.RoundaboutRight)
}

val screens = listOf(Screen.Home, Screen.Books, Screen.Cart, Screen.Statistics, Screen.Search)
