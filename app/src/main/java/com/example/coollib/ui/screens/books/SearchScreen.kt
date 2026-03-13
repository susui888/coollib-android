package com.example.coollib.ui.screens.books

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coollib.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    history: List<String>,
    onBack: () -> Unit,
    onClearAll: () -> Unit,
    onDeleteItem: (String) -> Unit,
    onSearch: (String) -> Unit)
{
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(true) }

    SearchBar(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it },
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = { text = it },
                onSearch = { onSearch(it) },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(stringResource(R.string.label_search_book_author)) },
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = "" }) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (history.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.label_newest_search), style = MaterialTheme.typography.titleSmall)
                        TextButton(onClick = onClearAll) {
                            Text(stringResource(R.string.label_clear))
                        }
                    }
                }

                items(history) { item ->
                    ListItem(
                        headlineContent = { Text(item) },
                        leadingContent = { Icon(Icons.Default.History, null) },
                        trailingContent = {
                            IconButton(onClick = { onDeleteItem(item) }) {
                                Icon(Icons.Default.Clear, null, modifier = Modifier.size(18.dp))
                            }
                        },
                        modifier = Modifier.clickable {
                            onSearch(item)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "With History")
@Composable
fun PreviewSearchWithHistory() {
    MaterialTheme {
        SearchScreen(
            history = listOf("Kotlin", "Jetpack Compose", "Android 15"),
            onBack = {},
            onClearAll = {},
            onDeleteItem = {},
            onSearch = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty History")
@Composable
fun PreviewSearchEmpty() {
    MaterialTheme {
        SearchScreen(
            history = emptyList(),
            onBack = {},
            onClearAll = {},
            onDeleteItem = {},
            onSearch = {}
        )
    }
}