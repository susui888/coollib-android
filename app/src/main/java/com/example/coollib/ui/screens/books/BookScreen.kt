package com.example.coollib.ui.screens.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.ui.components.BookCard
import com.example.coollib.ui.components.BookRow
import com.example.coollib.ui.mapper.toUiModel
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun BookScreen(
    viewModel: BookViewModel = hiltViewModel(),
    query: SearchQuery,
    onBookClick: (Int) -> Unit
){
    LaunchedEffect(Unit) {
        viewModel.searchBooks(query)
    }

    val books by viewModel.books.collectAsStateWithLifecycle()

    BookScreenContent(
        books = books,
        onBookClick = onBookClick
    )
}

@Composable
fun BookScreenContent(
    books: List<Book>,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var listMode by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${books.size} Books Found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { listMode = !listMode }
            ) {
                Icon(
                    imageVector =
                        if (listMode) Icons.Default.GridOn
                        else Icons.AutoMirrored.Filled.ViewList,
                    contentDescription = "Toggle View"
                )
            }
        }

        when (listMode){
            true -> BookList(books, onBookClick)
            else -> BookGrid(books, onBookClick)
        }
    }
}

@Composable
fun BookGrid(
    books: List<Book>,
    onBookClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            BookCard(book.toUiModel(), onBookClick)
        }
    }
}

@Composable
fun BookList(
    books: List<Book>,
    onBookClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books) { book ->
            BookRow(
                book = book.toUiModel(),
                onBookClick = onBookClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BooksScreenPreview() {
    CoolLibTheme {
        BookScreenContent(MockBooks.list, onBookClick = {})
    }
}
