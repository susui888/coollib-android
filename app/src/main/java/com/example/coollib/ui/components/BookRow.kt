package com.example.coollib.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.coollib.ui.mapper.toUiModel
import com.example.coollib.ui.model.BookItemUiModel
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun BookRow(
    book: BookItemUiModel,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = { onBookClick(book.id) },
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .border(1.dp, Color.LightGray),
                contentScale = ContentScale.Crop,
                placeholder = paintBookCover(book.title,book.author),
                error = paintBookCover(book.title,book.author)
            )

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {

                Text(
                    book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookRowPreview() {
    CoolLibTheme {
        BookRow(
            book = MockBooks.list.first().toUiModel(),
            onBookClick = {}
        )
    }
}