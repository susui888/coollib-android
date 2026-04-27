package com.example.coollib.ui.screens.books

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.coollib.ui.previewSupport.MockBooks
import org.junit.Rule
import org.junit.Test

class BookDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookTitle_isDisplayed() {

        val book = MockBooks.list.first()

        composeTestRule.setContent {
            BookDetailScreenContent(
                book = book,
                isInCart = false,
                isFavorite = false,
                onToggleCart = {},
                onToggleFavorite = {},
                onAuthorClick = {},
                onPublisherClick = {},
                onYearClick = {},
                onPostReview = {_ , _ -> },
            )
        }

        composeTestRule
            .onNodeWithText(book.title)
            .assertIsDisplayed()
    }

    @Test
    fun authorClick_triggersCallback() {

        val book = MockBooks.list.first()
        var clickedAuthor: String? = null

        composeTestRule.setContent {
            BookDetailScreenContent(
                book = book,
                isInCart = false,
                isFavorite = false,
                onToggleCart = {},
                onToggleFavorite = {},
                onAuthorClick = { clickedAuthor = it },
                onPublisherClick = {},
                onYearClick = {},
                onPostReview = { _, _ -> },
            )
        }

        composeTestRule
            .onNodeWithText("Author: ${book.author}", substring = true)
            .performClick()

        assert(clickedAuthor == book.author)
    }

    @Test
    fun addToCartButton_isDisplayed() {

        composeTestRule.setContent {
            BookDetailScreenContent(
                book = MockBooks.list.first(),
                isInCart = false,
                isFavorite = false,
                onToggleCart = {},
                onToggleFavorite = {},
                onAuthorClick = {},
                onPublisherClick = {},
                onYearClick = {},
                onPostReview = { _, _ -> },
            )
        }

        composeTestRule
            .onNodeWithText("Add to Cart")
            .assertIsDisplayed()
    }

    @Test
    fun favoriteButton_changesText() {

        composeTestRule.setContent {
            BookDetailScreenContent(
                book = MockBooks.list.first(),
                isInCart = false,
                isFavorite = true,
                onToggleCart = {},
                onToggleFavorite = {},
                onAuthorClick = {},
                onPublisherClick = {},
                onYearClick = {},
                onPostReview = { _, _ -> },
            )
        }

        composeTestRule
            .onNodeWithText("Remove")
            .assertIsDisplayed()
    }
}