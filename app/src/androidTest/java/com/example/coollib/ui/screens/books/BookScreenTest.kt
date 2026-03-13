package com.example.coollib.ui.screens.books

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.coollib.ui.previewSupport.MockBooks
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookCount_isDisplayed() {

        composeTestRule.setContent {
            BookScreenContent(
                books = MockBooks.list,
                onBookClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("BookCount")
            .assertIsDisplayed()
    }

    @Test
    fun toggleView_changesLayout() {

        composeTestRule.setContent {
            BookScreenContent(
                books = MockBooks.list,
                onBookClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("ToggleView")
            .performClick()

        composeTestRule
            .onNodeWithTag("ToggleView")
            .performClick()
    }


    @Test
    fun bookItem_click_triggersCallback() {
        val book = MockBooks.list.first()
        var clickedId: Int? = null

        composeTestRule.setContent {
            BookList(
                books = MockBooks.list,
                onBookClick = { clickedId = it }
            )
        }

        composeTestRule
            .onNodeWithTag("Book_${book.id}")
            .assertExists()
            .performClick()

        assertEquals(book.id, clickedId)
    }
}