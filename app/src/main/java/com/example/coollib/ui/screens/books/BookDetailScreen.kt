package com.example.coollib.ui.screens.books

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.coollib.R
import com.example.coollib.domain.model.Book
import com.example.coollib.ui.components.paintBookCover
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.screens.checkout.CartViewModel
import com.example.coollib.ui.screens.checkout.WishlistViewModel
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun BookDetailScreen(
    bookId: Int,
    bookViewModel: BookViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    onAuthorClick: (String) -> Unit,
    onPublisherClick: (String) -> Unit,
    onYearClick: (Int) -> Unit,
) {
    val selectedBook by bookViewModel.selectedBook.collectAsStateWithLifecycle()
    val isInCart by cartViewModel.isBookInCart(bookId).collectAsStateWithLifecycle(initialValue = false)
    val isFavorite by wishlistViewModel.isBookInWishlist(bookId).collectAsStateWithLifecycle(initialValue = false)
    val isInWishlist by wishlistViewModel.isBookInWishlist(bookId).collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(bookId) {
        bookViewModel.selectBook(bookId)
    }

    selectedBook?.let { book ->
        BookDetailScreenContent(
            book = book,
            isInCart = isInCart,
            isFavorite = isFavorite,
            onToggleCart = {
                cartViewModel.toggleCart(bookId, isInCart)
            },
            onToggleFavorite = {
                wishlistViewModel.toggleWishlist(bookId,isInWishlist)
            },
            onAuthorClick = onAuthorClick,
            onPublisherClick = onPublisherClick,
            onYearClick = onYearClick
        )
    }
}

@Composable
fun BookDetailScreenContent(
    book: Book,
    isInCart: Boolean,
    isFavorite: Boolean,
    onToggleCart: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onPublisherClick: (String) -> Unit,
    onYearClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(vertical = 24.dp)
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                modifier = Modifier
                    .size(width = 180.dp, height = 270.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
                    .shadow(8.dp)
                    .border(1.dp, Color.LightGray)
                    .clip(RectangleShape),
                contentScale = ContentScale.FillBounds,
                placeholder = paintBookCover(book.title, book.author)
            )

            Text(
                book.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${stringResource(R.string.label_author)}: ${book.author}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAuthorClick(book.author) }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )

            Text(
                text = "${stringResource(R.string.label_publisher)}: ${book.publisher}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPublisherClick(book.publisher) }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )

            Text(
                text = stringResource(R.string.label_year_format, book.year),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onYearClick(book.year) }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            val status = when (book.available) {
                true -> stringResource(R.string.status_available)
                else -> stringResource(R.string.status_checked_out)
            }

            Text(
                text = stringResource(R.string.label_status, status),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = if (book.available) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                stringResource(
                    R.string.label_description,
                    book.description ?: stringResource(R.string.label_no_description)
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CartButton(
                available = book.available,
                isBookInCart = isInCart,
                onClick = onToggleCart,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
            )


            Spacer(Modifier.size(12.dp))

            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onToggleFavorite,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
fun CartButton(
    available: Boolean,
    isBookInCart: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val containerColor = when {
        !available -> MaterialTheme.colorScheme.surfaceVariant // 不可用时：灰色
        isBookInCart -> MaterialTheme.colorScheme.errorContainer // 已在篮子：红色/粉色（提示移除）
        else -> MaterialTheme.colorScheme.primary // 可借阅且不在篮子：主色（蓝色）
    }

    val contentColor = when {
        !available -> MaterialTheme.colorScheme.onSurfaceVariant
        isBookInCart -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onPrimary
    }

    Button(
        onClick = onClick,
        enabled = available,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {

        Icon(
            imageVector = when {
                !available -> Icons.Default.Block
                isBookInCart -> Icons.Default.RemoveShoppingCart
                else -> Icons.Default.AddShoppingCart
            },
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.size(8.dp))
        Text(
            text = when {
                !available -> "Checked out"      // 已借出
                isBookInCart -> "Remove"         // 从借书篮移除
                else -> "Add to Cart"            // 加入借书篮
            },
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val containerColor =
        if (isFavorite)
            MaterialTheme.colorScheme.errorContainer
        else
            MaterialTheme.colorScheme.surfaceVariant

    Button(
        onClick = onClick,
        modifier = modifier
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        shape = MaterialTheme.shapes.medium
    ) {

        Icon(
            imageVector =
                if (isFavorite)
                    Icons.Default.Favorite
                else
                    Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint =
                if (isFavorite)
                    MaterialTheme.colorScheme.error
                else
                    LocalContentColor.current
        )

        Spacer(Modifier.size(8.dp))

        Text(
            text = if (isFavorite) "Remove" else "Favorite"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailScreenPreview() {
    CoolLibTheme {
        val context = LocalContext.current
        BookDetailScreenContent(
            book = MockBooks.list.first(),
            isInCart = true,
            isFavorite = true,
            onToggleCart = {},
            onToggleFavorite = {},
            onAuthorClick = { author ->
                Toast.makeText(context, author, Toast.LENGTH_LONG).show() },
            onPublisherClick = {},
            onYearClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailScreenPreview_NoInCart() {
    CoolLibTheme {
        BookDetailScreenContent(
            book = MockBooks.list.first(),
            isInCart = false,
            isFavorite = true,
            onToggleCart = {},
            onToggleFavorite = {},
            onAuthorClick = {},
            onPublisherClick = {},
            onYearClick = {}
        )
    }
}