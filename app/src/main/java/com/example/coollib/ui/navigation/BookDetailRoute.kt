package com.example.coollib.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.ui.screens.books.BookDetailScreen

fun NavGraphBuilder.bookDetailRoute(
    navController: NavController
) {
    composable(
        route = Screen.BookDetail.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: return@composable

        BookDetailScreen(
            bookId = id,
            onAuthorClick = { author ->
                navController.navigate(Screen.Books.createRoute(SearchQuery(author = author)))
            },
            onPublisherClick = { publisher ->
                navController.navigate(Screen.Books.createRoute(SearchQuery(publisher = publisher) ))
            },
            onYearClick = { year ->
                navController.navigate(Screen.Books.createRoute(SearchQuery(year = year)))
            },
        )
    }
}