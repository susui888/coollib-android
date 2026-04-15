package com.example.coollib.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.coollib.R
import com.example.coollib.domain.model.Loan
import com.example.coollib.domain.model.LoanStatus
import com.example.coollib.ui.components.BookCoverImage
import com.example.coollib.ui.components.paintBookCover
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockLoans
import com.example.coollib.ui.theme.CoolLibTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LoanScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    viewModel: LoanViewModel = hiltViewModel()
) {
    val loans by viewModel.loans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LoanContent(
        loans = loans,
        isLoading = isLoading,
        isLoggedIn = isLoggedIn,
        onBack = onBack,
        onLogin = onLogin,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanContent(
    loans: List<Loan>,
    isLoading: Boolean,
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.loan_history_label)) },
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
                isLoading && loans.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                loans.isEmpty() -> {
                    Text(
                        text = "No loan records found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(loans) { loan ->
                            LoanItem(loan = loan)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginPrompt(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.please_login),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.login_button))
        }
    }
}

@Composable
fun LoanItem(loan: Loan) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                 BookCoverImage(
                    url = loan.book?.coverUrl,
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = loan.book?.title ?: "Unknown Book",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Text(
                        text = loan.book?.author ?: "Unknown Author",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    StatusBadge(status = loan.status)
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateInfo(label = "Borrowed", date = loan.borrowDate.format(dateFormatter))
                DateInfo(label = "Due Date", date = loan.dueDate.format(dateFormatter))
                if (loan.returnDate != null) {
                    DateInfo(label = "Returned", date = loan.returnDate!!.format(dateFormatter))
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: LoanStatus) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(status.displayColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.description,
            style = MaterialTheme.typography.labelSmall,
            color = status.displayColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DateInfo(label: String, date: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoanScreenPreview() {
    CoolLibTheme {
        LoanContent(
            loans = MockLoans.list, 
            isLoading = false,
            isLoggedIn = true,
            onBack = {},
            onLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoanScreenNotLoggedInPreview() {
    CoolLibTheme {
        LoanContent(
            loans = emptyList(),
            isLoading = false,
            isLoggedIn = false,
            onBack = {},
            onLogin = {}
        )
    }
}
