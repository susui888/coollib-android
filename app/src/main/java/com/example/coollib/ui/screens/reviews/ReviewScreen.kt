package com.example.coollib.ui.screens.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.coollib.R
import com.example.coollib.domain.model.Review
import com.example.coollib.domain.model.TelemetryEvents
import com.example.coollib.ui.components.TrackScreenView
import com.example.coollib.ui.components.book.BookCoverImage
import com.example.coollib.ui.components.login.LoginPrompt
import com.example.coollib.ui.previewSupport.MockReviews
import com.example.coollib.ui.theme.CoolLibTheme
import com.example.coollib.ui.util.toReadableDate

@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    TrackScreenView(screenName = TelemetryEvents.Screens.REVIEW)

    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoggedIn = viewModel.isLoggedIn

    ReviewContent(
        reviews = reviews,
        isLoading = isLoading,
        isLoggedIn = isLoggedIn,
        onBack = onBack,
        onLogin = onLogin,
        onDelete = { review -> viewModel.deleteReview(review) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewContent(
    reviews: List<Review>,
    isLoading: Boolean,
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onDelete: (review: Review) -> Unit,
    modifier: Modifier = Modifier
) {
    var reviewToDelete by remember { mutableStateOf<Review?>(null) }

    if (reviewToDelete != null) {
        AlertDialog(
            onDismissRequest = { reviewToDelete = null },
            title = { Text("Delete Review?") },
            text = { Text("This action will permanently remove your review and associated images from CoolLib. This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        reviewToDelete?.let { onDelete(it) }
                        reviewToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { reviewToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.reviews_label)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = modifier.padding(padding).fillMaxSize()) {
            when {
                !isLoggedIn -> {
                    LoginPrompt(
                        onLoginClick = onLogin,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                isLoading && reviews.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                reviews.isEmpty() -> {
                    Text(
                        text = "No reviews found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = reviews,
                            key = { it.id?: 0 }
                        ) { review ->

                            val dismissState = rememberSwipeToDismissBoxState()

                            // 2. 使用 LaunchedEffect 监听 currentValue 的改变
                            LaunchedEffect(dismissState.currentValue) {
                                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                    reviewToDelete = review
                                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                                }
                            }

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false, // 只允许从右向左滑
                                backgroundContent = {
                                    val color = when (dismissState.dismissDirection) {
                                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                        else -> Color.Transparent
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CardDefaults.elevatedShape)
                                            .background(color)
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
                                ReviewHistoryItem(review = review)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewHistoryItem(review: Review) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 顶部：书籍信息
            Row(verticalAlignment = Alignment.CenterVertically) {
                BookCoverImage(
                    url = review.book?.coverUrl,
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.book?.title ?: "Unknown Book",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Text(
                        text = review.book?.author ?: "Unknown Author",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 评分展示
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (index < review.rating) Color(0xFFFFA000) else MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${review.rating}.0",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 中间分割线
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // 底部：评论内容、图片与日期
            Column(modifier = Modifier.fillMaxWidth()) {
                if (review.content.isNotEmpty()) {
                    Text(
                        text = review.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // 图片列表展示
                if (review.imageUrls.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(review.imageUrls) { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Review Image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = "Reviewed on ${review.createdAt.toReadableDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Reviews - Content")
@Composable
fun ReviewScreenPreview() {
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

@Preview(showBackground = true, name = "Reviews - Empty")
@Composable
fun ReviewScreenEmptyPreview() {
    CoolLibTheme {
        ReviewContent(
            reviews = emptyList(),
            isLoading = false,
            isLoggedIn = true,
            onBack = {},
            onLogin = {},
            onDelete = {},
        )
    }
}

@Preview(showBackground = true, name = "Reviews - Not Logged In")
@Composable
fun ReviewScreenNotLoggedInPreview() {
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