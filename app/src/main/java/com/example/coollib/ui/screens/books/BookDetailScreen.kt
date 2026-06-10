package com.example.coollib.ui.screens.books

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.coollib.R
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.ui.components.TrackScreenView
import com.example.coollib.ui.components.review.AddReviewSection
import com.example.coollib.ui.components.book.BookCoverImage
import com.example.coollib.ui.components.review.BookReviewSection
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockReviews
import com.example.coollib.ui.screens.checkout.CartViewModel
import com.example.coollib.ui.screens.checkout.WishlistViewModel
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    bookId: Int,
    bookViewModel: BookViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    onAuthorClick: (String) -> Unit,
    onPublisherClick: (String) -> Unit,
    onYearClick: (Int) -> Unit,
) {
    TrackScreenView(screenName = TelemetryEvents.Screens.BOOK_DETAIL)

    val selectedBook by bookViewModel.selectedBook.collectAsStateWithLifecycle()
    val reviews by bookViewModel.reviews.collectAsStateWithLifecycle()

    val isInCart by cartViewModel.isBookInCart(bookId)
        .collectAsStateWithLifecycle(initialValue = false)
    val isFavorite by wishlistViewModel.isBookInWishlist(bookId)
        .collectAsStateWithLifecycle(initialValue = false)
    val isInWishlist by wishlistViewModel.isBookInWishlist(bookId)
        .collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(bookId) {
        bookViewModel.selectBook(bookId)
    }

    selectedBook?.let { book ->
        BookDetailScreenContent(
            modifier = modifier,
            reviews = reviews,
            book = book,
            isInCart = isInCart,
            isFavorite = isFavorite,
            onToggleCart = {
                cartViewModel.toggleCart(bookId, isInCart)
            },
            onToggleFavorite = {
                wishlistViewModel.toggleWishlist(bookId, isInWishlist)
            },
            onAuthorClick = onAuthorClick,
            onPublisherClick = onPublisherClick,
            onYearClick = onYearClick,
            onPostReview = { rating, content, uris ->
                bookViewModel.postReview(book.id, rating, content, uris)
            },
        )
    }
}

@Composable
fun BookDetailScreenContent(
    modifier: Modifier = Modifier,
    book: Book,
    reviews: List<Review> = emptyList(),
    isInCart: Boolean,
    isFavorite: Boolean,
    onToggleCart: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onPublisherClick: (String) -> Unit,
    onYearClick: (Int) -> Unit,
    onPostReview: (Int, String, List<Uri>) -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {
    val dynamicColor = rememberDominantColor(book.coverUrl)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        dynamicColor.copy(alpha = 0.5f),
                        Color.Transparent
                    ),
                )
            )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BookCoverImage(
                url = book.coverUrl,
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(14.dp)),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                book.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
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
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = "${stringResource(R.string.label_publisher)}: ${book.publisher}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPublisherClick(book.publisher) }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = stringResource(R.string.label_year_format, book.year),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onYearClick(book.year) }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            val status = when (book.available) {
                true -> stringResource(R.string.status_available)
                else -> stringResource(R.string.status_checked_out)
            }

            Text(
                text = stringResource(R.string.label_status, status),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                color = if (book.available) Color(0xFF2E7D32) else Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                stringResource(
                    R.string.label_description,
                    book.description ?: stringResource(R.string.label_no_description)
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
            )


            AddReviewSection(
                onPostReview = onPostReview
            )

            BookReviewSection(reviews = reviews)

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
fun rememberDominantColor(imageUrl: String): Color {
    val context = LocalContext.current

    val defaultColor = Color(0xFF2B1B10)
    var dominantColor by remember(imageUrl) { mutableStateOf(defaultColor) }

    LaunchedEffect(imageUrl) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)
        if (result is SuccessResult) {
            val bitmap = (result.drawable as BitmapDrawable).bitmap
            Palette.from(bitmap).generate { palette ->
                val swatch = palette?.darkMutedSwatch
                    ?: palette?.darkVibrantSwatch
                    ?: palette?.dominantSwatch

                swatch?.rgb?.let { colorInt ->
                    dominantColor = Color(colorInt)
                }
            }
        }
    }
    return dominantColor
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
            .height(40.dp),
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
            .height(40.dp),
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

@Preview(showBackground = true, device = Devices.PIXEL_9,showSystemUi = true)
@Composable
fun BookDetailScreenPreview() {
    CoolLibTheme {
        val context = LocalContext.current
        BookDetailScreenContent(
            book = MockBooks.list.first(),
            reviews = MockReviews.list,
            isInCart = true,
            isFavorite = true,
            onToggleCart = {},
            onToggleFavorite = {},
            onAuthorClick = { author ->
                Toast.makeText(context, author, Toast.LENGTH_LONG).show()
            },
            onPublisherClick = {},
            onYearClick = {},
            onPostReview = { _, _, _ -> }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_9,showSystemUi = true)
@Composable
fun BookDetailScreenPreview_NoInCart() {
    CoolLibTheme {
        BookDetailScreenContent(
            book = MockBooks.list.last(),
            reviews = MockReviews.list,
            isInCart = false,
            isFavorite = true,
            onToggleCart = {},
            onToggleFavorite = {},
            onAuthorClick = {},
            onPublisherClick = {},
            onYearClick = {},
            onPostReview = { _, _,_ -> }
        )
    }
}