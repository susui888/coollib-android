package com.example.coollib.ui.screens.reviews

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.coollib.ui.previewSupport.MockReviews
import com.example.coollib.ui.theme.CoolLibTheme
import org.junit.Rule
import org.junit.Test

class ReviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginPrompt_shows_whenNotLoggedIn() {
        composeTestRule.setContent {
            CoolLibTheme {
                ReviewContent(
                    reviews = emptyList(),
                    isLoading = false,
                    isLoggedIn = false,
                    onBack = {},
                    onLogin = {},
                    onDelete = {}
                )
            }
        }

        // Assuming LoginPrompt has a unique text or tag
        composeTestRule.onNodeWithText("Login", substring = true).assertIsDisplayed()
    }

    @Test
    fun emptyState_shows_whenNoReviews() {
        composeTestRule.setContent {
            CoolLibTheme {
                ReviewContent(
                    reviews = emptyList(),
                    isLoading = false,
                    isLoggedIn = true,
                    onBack = {},
                    onLogin = {},
                    onDelete = {}
                )
            }
        }

        composeTestRule.onNodeWithText("No reviews found").assertIsDisplayed()
    }

    @Test
    fun reviewList_showsItems_whenLoggedInWithData() {
        // 1. Get a specific review that definitely has data
        val targetReview = MockReviews.list.firstOrNull { !it.book?.title.isNullOrBlank() }
            ?: throw AssertionError("MockReviews.list must contain at least one review with a title")

        val targetTitle = targetReview.book!!.title!!

        composeTestRule.setContent {
            CoolLibTheme {
                ReviewContent(
                    reviews = MockReviews.list,
                    isLoading = false,
                    isLoggedIn = true,
                    onBack = {},
                    onLogin = {},
                    onDelete = {}
                )
            }
        }

        // 2. Use onAllNodes...onFirst() to handle duplicates like "Unknown Book"
        // 3. Add .scrollTo() in case the item is further down the list
        composeTestRule
            .onAllNodesWithText(targetTitle)
            .onFirst()
            .performScrollTo() // Ensures the node is in the viewport
            .assertIsDisplayed()

    }

    @Test
    fun swipeToDelete_showsConfirmationDialog() {
        var deleteCalled = false
        val reviews = listOf(MockReviews.list.first())

        composeTestRule.setContent {
            CoolLibTheme {
                ReviewContent(
                    reviews = reviews,
                    isLoading = false,
                    isLoggedIn = true,
                    onBack = {},
                    onLogin = {},
                    onDelete = { deleteCalled = true }
                )
            }
        }

        // 1. Swipe the first item from right to left (End to Start)
        composeTestRule.onNodeWithText(reviews.first().book?.title ?: "")
            .performTouchInput {
                swipeLeft()
            }

        // 2. Check if the Delete dialog appears
        composeTestRule.onNodeWithText("Delete Review?").assertIsDisplayed()

        // 3. Click Delete button in dialog
        composeTestRule.onNodeWithText("Delete").performClick()

        // 4. Verify callback was triggered
        assert(deleteCalled)
    }
}