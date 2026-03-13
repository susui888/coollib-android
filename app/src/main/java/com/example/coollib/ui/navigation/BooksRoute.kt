package com.example.coollib.ui.navigation

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.ui.screens.books.BookScreen

fun NavGraphBuilder.booksRoute(
    navController: NavController
) {

    composable(
        route = Screen.Books.route,
        arguments = listOf(
            navArgument("type") { type = NavType.StringType },
            navArgument("value") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val type = backStackEntry.arguments?.getString("type") ?: ""
        val value = backStackEntry.arguments?.getString("value")

        val query = when (type) {

            "category" ->
                SearchQuery(category = value?.toInt())

            "author" ->
                SearchQuery(author = value)

            "publisher" ->
                SearchQuery(publisher = value)

            "year" ->
                SearchQuery(year = value?.toInt())

            "search" ->
                SearchQuery(searchTerm = value)

            else ->
                SearchQuery()
        }

        BookScreen(
            modifier = Modifier.testTag("BookScreen"),
            query = query,
            onBookClick = { id ->
                navController.navigate(Screen.BookDetail.createRoute(id))
            }
        )
    }
}