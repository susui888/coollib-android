package com.example.coollib.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coollib.R

sealed class Screen(
    val route: String,
    @param:StringRes val labelResourceId: Int,
    val icon: ImageVector
) {
    data object Home: Screen("home", R.string.home_label, Icons.Rounded.Home)
    data object Books: Screen("books", R.string.books_label, Icons.Rounded.Book)
    data object Cart: Screen("cart", R.string.cart_label, Icons.Rounded.ShoppingBasket)
    data object Statistics: Screen("statistics",R.string.statistics_label,Icons.Rounded.Analytics)
    data object Search: Screen("search", R.string.search_label,Icons.Rounded.Search)

}

val screens = listOf(Screen.Home, Screen.Books, Screen.Cart, Screen.Statistics, Screen.Search)
