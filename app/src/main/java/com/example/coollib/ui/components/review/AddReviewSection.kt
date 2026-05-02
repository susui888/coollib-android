package com.example.coollib.ui.components.review

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun AddReviewSection(
    onPostReview: (Int, String, List<Uri>) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var content by remember { mutableStateOf("") }

    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris -> selectedImages = selectedImages + uris }


    Spacer(modifier = Modifier.height(32.dp))

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "What do you think?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                (1..5).forEach { index ->
                    val isSelected = index <= rating

                    IconButton(
                        onClick = { rating = index },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Rate $index stars",

                            tint = if (isSelected) {
                                Color(0xFFFFA000)
                            } else {
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }


            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Share your reading experience...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )

            ImageAttachmentRow(
                images = selectedImages,
                onRemoveImage = { uri -> selectedImages = selectedImages - uri }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Add photos",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = {
                        if (content.isNotBlank()) {
                            onPostReview(rating, content, selectedImages)
                            content = ""
                            selectedImages = emptyList()
                        }
                    },
                    enabled = content.isNotBlank(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Post Review")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Add Review Card")
@Composable
fun AddReviewSectionPreview() {
    CoolLibTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            AddReviewSection(
                onPostReview = { rating, content, images ->
                    println("Post review: $rating stars, content: $content, images count: ${images.size}")
                }
            )
        }
    }
}