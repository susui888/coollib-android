package com.example.coollib.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    onSeeMoreLoans: () -> Unit = {},
    onLogin: () -> Unit = {},
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (!state.isLoggedIn) {
            LoginPrompt(
                onLoginClick = onLogin,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            StatisticsContent(
                state = state,
                onSeeMoreLoans = onSeeMoreLoans
            )
        }
    }
}

@Composable
fun StatisticsContent(
    state: StatisticsState,
    onSeeMoreLoans: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        StatCard(
            title = "Currently Borrowed",
            value = state.currentlyBorrowed.toString(),
            icon = Icons.AutoMirrored.Filled.MenuBook
        )

        StatCard(
            title = "Due Soon",
            value = state.dueSoon.toString(),
            icon = Icons.Default.Schedule
        )

        StatCard(
            title = "Overdue",
            value = state.overdue.toString(),
            icon = Icons.Default.Warning
        )

        StatCard(
            title = "Total Borrowed",
            value = state.totalBorrowed.toString(),
            icon = Icons.AutoMirrored.Filled.LibraryBooks
        )

        Button(
            onClick = onSeeMoreLoans,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("See Loan History")
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    CoolLibTheme {
        StatisticsContent(
            state = StatisticsState(
                currentlyBorrowed = 3,
                dueSoon = 1,
                overdue = 0,
                totalBorrowed = 27,
                isLoggedIn = true
            ),
            onSeeMoreLoans = {}
        )
    }
}
