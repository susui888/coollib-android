package com.example.coollib.ui.components.review

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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


@Composable
fun BookReviewSection(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
) {
    val reviewCount = reviews.size
    val averageRating = if (reviews.isNotEmpty()) {
        reviews.map { it.rating }.average()
    } else {
        0.0
    }

    Spacer(modifier = Modifier.height(32.dp))

    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Reader Feedback",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (reviewCount > 0) "Based on $reviewCount reviews" else "No ratings yet",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (reviewCount > 0) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%.1f", averageRating),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }


        if (reviews.isEmpty()) {
            Text(
                text = "Be the first to rate this book!",
                modifier = Modifier.padding(32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            reviews.sortedByDescending { it.createdAt }.forEach { review ->
                ReviewItem(review = review)
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

