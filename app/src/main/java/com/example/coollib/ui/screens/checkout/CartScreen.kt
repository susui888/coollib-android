package com.example.coollib.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coollib.R
import com.example.coollib.domain.model.Cart
import com.example.coollib.domain.model.Wishlist
import com.example.coollib.ui.components.BookRow
import com.example.coollib.ui.mapper.toUiModel
import com.example.coollib.ui.previewSupport.MockCart
import com.example.coollib.ui.previewSupport.MockWishlist
import com.example.coollib.ui.theme.CoolLibTheme

enum class CartTab {
    CART,
    WISHLIST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<Cart>,
    wishlistItems: List<Wishlist>,
    isBorrowing: Boolean,
    onBookClick: (Int) -> Unit,
    onRemoveCartItem: (Int) -> Unit,
    onRemoveWishlistItem: (Int) -> Unit,
    onBorrow: () -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedTab by remember { mutableStateOf(CartTab.CART) }

    Scaffold(
        bottomBar = {

            if (selectedTab == CartTab.CART && cartItems.isNotEmpty()) {
                BorrowButtonBar(
                    count = cartItems.size,
                    isLoading = isBorrowing,
                    onBorrowClick = onBorrow
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            CartSegmentedPicker(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            when (selectedTab) {

                CartTab.CART -> CartList(
                    items = cartItems,
                    onBookClick = onBookClick,
                    onDelete = onRemoveCartItem
                )

                CartTab.WISHLIST -> WishlistList(
                    items = wishlistItems,
                    onBookClick = onBookClick,
                    onDelete = onRemoveWishlistItem
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSegmentedPicker(
    selectedTab: CartTab,
    onTabSelected: (CartTab) -> Unit
) {

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        SegmentedButton(
            selected = selectedTab == CartTab.CART,
            onClick = { onTabSelected(CartTab.CART) },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 2
            )
        ) {
            Text(stringResource(R.string.cart_label))
        }

        SegmentedButton(
            selected = selectedTab == CartTab.WISHLIST,
            onClick = { onTabSelected(CartTab.WISHLIST) },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 2
            )
        ) {
            Text(stringResource(R.string.wishlist_label))
        }
    }
}

@Composable
fun CartList(
    items: List<Cart>,
    onBookClick: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {

    if (items.isEmpty()) {

        EmptyView(
            icon = Icons.Default.ShoppingCart,
            title = stringResource(R.string.cart_is_empty),
            subtitle = stringResource(R.string.empty_msg)
        )

    } else {

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(items, key = { it.id }) { cart ->

                SwipeToDeleteRow(
                    onDelete = { onDelete(cart.id) }
                ) {
                    BookRow(
                        book = cart.toUiModel(),
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistList(
    items: List<Wishlist>,
    onBookClick: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {

    if (items.isEmpty()) {

        EmptyView(
            icon = Icons.Default.FavoriteBorder,
            title = stringResource(R.string.no_saved_books),
            subtitle = stringResource(R.string.empty_msg)
        )

    } else {

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(items, key = { it.id }) { wishlist ->

                SwipeToDeleteRow(
                    onDelete = { onDelete(wishlist.id) }
                ) {

                    BookRow(
                        book = wishlist.toUiModel(),
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}

@Composable
fun BorrowButtonBar(
    count: Int,
    isLoading: Boolean,
    onBorrowClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Button(
                onClick = onBorrowClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = stringResource(R.string.confirm_borrow_books, count),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyView(
    icon: ImageVector,
    title: String,
    subtitle: String
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteRow(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {

    val swipeState = rememberSwipeToDismissBoxState()
    val shape = MaterialTheme.shapes.medium

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Surface(
            shape = shape
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {

    CoolLibTheme {
        CartScreen(
            cartItems = MockCart.list,
            wishlistItems = MockWishlist.list,
            isBorrowing = false,
            onBookClick = {},
            onRemoveCartItem = {},
            onRemoveWishlistItem = {},
            onBorrow = {}
        )
    }
}