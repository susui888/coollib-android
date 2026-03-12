package com.example.coollib.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Home
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
    //data object Books: Screen("books", R.string.books_label, Icons.Rounded.Book)
    data object Books : Screen("books/{type}/{value}", R.string.books_label, Icons.Rounded.Book) {

        fun createRoute(query: SearchQuery): String {

            return when (query.searchType) {

                SearchType.CATEGORY ->
                    "books/category/${query.category}"

                SearchType.AUTHOR ->
                    "books/author/${query.author}"

                SearchType.PUBLISHER ->
                    "books/publisher/${query.publisher}"

                SearchType.YEAR ->
                    "books/year/${query.year}"

                SearchType.SEARCH ->
                    "books/search/${query.searchTerm}"

                SearchType.ALL ->
                    "books/all/0"
            }
        }
    }

    data object Cart: Screen("cart", R.string.cart_label, Icons.Rounded.ShoppingBasket)
    data object Statistics: Screen("statistics",R.string.statistics_label,Icons.Rounded.Analytics)
    data object Search: Screen("search", R.string.search_label,Icons.Rounded.Search)

    //Nested screen
    data object BookDetail: Screen("bookDetail/{id}", 0, Icons.Rounded.Book){
        fun createRoute(id: Int) = "bookDetail/$id"
    }
}

val screens = listOf(Screen.Home, Screen.Books, Screen.Cart, Screen.Statistics, Screen.Search)
