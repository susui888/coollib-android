package com.example.coollib.ui.screens.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.example.coollib.R
import com.example.coollib.ui.mapper.toUiModel
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockCategory
import com.example.coollib.ui.previewSupport.MockWishlist
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenContent_isDisplayed() {
        composeTestRule.setContent {
            HomeScreenContent(
                categoryList = MockCategory.list,
                lastViewBooks = MockBooks.list.map { it.toUiModel() },
                wishlist = MockWishlist.list.map { it.toUiModel() },
                newestBooks = MockBooks.list.map { it.toUiModel() },
                onCategoryClick = {},
                onBookClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("HomeScreenContent")
            .assertIsDisplayed()
    }

    @Test
    fun category_click_triggersCallback() {
        var clickedCategoryId: Int? = null

        composeTestRule.setContent {
            HomeScreenContent(
                categoryList = MockCategory.list,
                lastViewBooks = emptyList(),
                wishlist = emptyList(),
                newestBooks = emptyList(),
                onCategoryClick = { clickedCategoryId = it },
                onBookClick = {}
            )
        }

        val firstCategory = MockCategory.list.first()

        composeTestRule
            .onNodeWithTag("CategoryLazyRow")
            .performScrollToNode(hasTestTag("Category_${firstCategory.id}"))
            .performClick()

        assertEquals(firstCategory.id, clickedCategoryId)
    }

    @Test
    fun book_click_triggersCallback() {
        var clickedBookId: Int? = null

        composeTestRule.setContent {
            HomeScreenContent(
                categoryList = emptyList(),
                lastViewBooks = MockBooks.list.map { it.toUiModel() },
                wishlist = emptyList(),
                newestBooks = emptyList(),
                onCategoryClick = {},
                onBookClick = { clickedBookId = it }
            )
        }

        composeTestRule
            .onNodeWithTag("BookRowLazyRow_${R.string.recently_viewed}")
            .performScrollToNode(hasTestTag("Book_${MockBooks.list.first().id}"))
            .performClick()

        assert(clickedBookId != null) { "Book click callback should be triggered" }

        assert(MockBooks.list.any { it.id == clickedBookId }) { "Clicked book ID should be in the mock list" }
    }

    @Test
    fun sectionTitles_areDisplayed() {
        composeTestRule.setContent {
            HomeScreenContent(
                categoryList = emptyList(),
                lastViewBooks = MockBooks.list.map { it.toUiModel() },
                wishlist = MockWishlist.list.map { it.toUiModel() },
                newestBooks = MockBooks.list.map { it.toUiModel() },
                onCategoryClick = {},
                onBookClick = {}
            )
        }

        // 检查每个 SectionTitle 是否显示
        val titlesRes = listOf(
            R.string.explore_categories,
            R.string.recently_viewed,
            R.string.wishlist_label,
            R.string.new_arrival
        )

        titlesRes.forEach { resId ->
            composeTestRule
                .onNodeWithTag("SectionTitle_$resId")
                .assertIsDisplayed()
        }
    }
}