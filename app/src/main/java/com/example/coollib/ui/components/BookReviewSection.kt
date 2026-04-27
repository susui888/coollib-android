package com.example.coollib.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coollib.domain.model.Review
import com.example.coollib.ui.previewSupport.MockReviews
import com.example.coollib.ui.theme.CoolLibTheme
import com.example.coollib.ui.util.toReadableDate

@Composable
fun BookReviewSection(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Reader Feedback (${reviews.size})",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (reviews.isEmpty()) {
            Text(
                text = "No one has shared their thoughts yet. Be the first!",
                modifier = Modifier.padding(32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            reviews.forEach { review ->
                ReviewItem(review = review)
            }
        }
    }
}

/**
 * 根据 UserId 生成一个生动的颜色
 */
@Composable
private fun rememberAvatarColor(userId: Int): Color {
    val colors = listOf(
        Color(0xFFEF5350), // Red
        Color(0xFFEC407A), // Pink
        Color(0xFFAB47BC), // Purple
        Color(0xFF7E57C2), // Deep Purple
        Color(0xFF42A5F5), // Blue
        Color(0xFF26A69A), // Teal
        Color(0xFF9CCC65), // Light Green
        Color(0xFFFFCA28)  // Amber
    )
    return colors[userId % colors.size]
}

@Composable
fun ReviewItem(
    modifier: Modifier = Modifier,
    review: Review,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // --- 顶部区域：头像、用户、时间 ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 生动的字母头像
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(rememberAvatarColor(review.userId), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "U", // 简写
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 用户名和时间
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reader ${review.userId}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = review.createdAt.toReadableDate(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // --- 中部区域：评论内容 ---
            if (review.content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = review.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
                )
            }

            // --- 底部区域：评分星星 ---
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "★ ".repeat(review.rating),
                    color = Color(0xFFFFC107),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "☆ ".repeat(5 - review.rating),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}



@Preview(showBackground = true, name = "Reviews List")
@Composable
fun BookReviewSectionPreview() {
    CoolLibTheme {
        BookReviewSection(
            reviews = MockReviews.list
        )
    }
}

@Preview(showBackground = true, name = "Empty Reviews")
@Composable
fun BookReviewSectionEmptyPreview() {
    CoolLibTheme {
        BookReviewSection(
            reviews = emptyList()
        )
    }
}

@Preview(showBackground = true, name = "Single Review Item")
@Composable
fun ReviewItemPreview() {
    CoolLibTheme {
        ReviewItem(
            review = MockReviews.list.first()
        )
    }
}