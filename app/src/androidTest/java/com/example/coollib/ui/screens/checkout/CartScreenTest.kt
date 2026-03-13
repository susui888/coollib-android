package com.example.coollib.ui.screens.checkout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.coollib.ui.previewSupport.MockCart
import com.example.coollib.ui.previewSupport.MockWishlist
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cartTab_displaysCartItems() {
        var clickedId: Int? = null

        composeTestRule.setContent {
            CartScreenContent(
                cartItems = MockCart.list,
                wishlistItems = MockWishlist.list,
                isBorrowing = false,
                onBookClick = { clickedId = it },
                onRemoveCartItem = {},
                onRemoveWishlistItem = {},
                onBorrow = {}
            )
        }

        // 点击第一个 Cart 项
        val firstCart = MockCart.list.first()
        composeTestRule
            .onNodeWithTag("Book_${firstCart.id}")
            .assertIsDisplayed()
            .performClick()

        assertEquals(firstCart.id, clickedId)
    }

    @Test
    fun wishlistTab_displaysWishlistItems() {
        var clickedId: Int? = null

        composeTestRule.setContent {
            CartScreenContent(
                cartItems = emptyList(),
                wishlistItems = MockWishlist.list,
                isBorrowing = false,
                onBookClick = { clickedId = it },
                onRemoveCartItem = {},
                onRemoveWishlistItem = {},
                onBorrow = {}
            )
        }

        // 切换到 Wishlist tab
        composeTestRule.onNodeWithTag("Tab_Wishlist").performClick()

        val firstWishlist = MockWishlist.list.first()
        composeTestRule
            .onNodeWithTag("Book_${firstWishlist.id}")
            .assertIsDisplayed()
            .performClick()

        assertEquals(firstWishlist.id, clickedId)
    }

    @Test
    fun borrowButton_isDisplayedAndClickable() {
        var clicked = false

        composeTestRule.setContent {
            CartScreenContent(
                cartItems = MockCart.list,
                wishlistItems = emptyList(),
                isBorrowing = false,
                onBookClick = {},
                onRemoveCartItem = {},
                onRemoveWishlistItem = {},
                onBorrow = { clicked = true }
            )
        }

        composeTestRule
            .onNodeWithTag("BorrowButton")
            .assertIsDisplayed()
            .performClick()

        assertEquals(true, clicked)
    }

    @Test
    fun emptyCart_showsEmptyView() {
        composeTestRule.setContent {
            CartScreenContent(
                cartItems = emptyList(),
                wishlistItems = emptyList(),
                isBorrowing = false,
                onBookClick = {},
                onRemoveCartItem = {},
                onRemoveWishlistItem = {},
                onBorrow = {}
            )
        }

        composeTestRule
            .onNodeWithTag("EmptyCartView")
            .assertIsDisplayed()
    }
}