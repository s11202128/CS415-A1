package com.bof.mobile.ui.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bof.mobile.viewmodel.AccountsViewModel

@Composable
fun AccountsScreen(viewModel: AccountsViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAccounts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Accounts", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.isLoadingAccounts) {
            CircularProgressIndicator()
            return@Column
        }

        if (!uiState.errorMessage.isNullOrBlank()) {
            Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = viewModel::loadAccounts) { Text("Retry") }
        }

        if (uiState.accounts.isEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("No accounts found")
            return@Column
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(uiState.accounts) { account ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text("${account.accountNumber} (${account.type})")
                    Text("Balance: FJD ${String.format("%.2f", account.balance)}")
                    OutlinedButton(onClick = { viewModel.selectAccount(account.id) }) {
                        Text("View details")
                    }
                }
            }
        }

        val details = uiState.selectedAccountDetails
        if (details != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Selected Account", style = MaterialTheme.typography.titleMedium)
            Text("Holder: ${details.customer.fullName}")
            Text("Status: ${details.account.status}")
            Text("Recent Transactions", style = MaterialTheme.typography.titleSmall)

            details.transactions.take(5).forEach { tx ->
                Text("${tx.kind.uppercase()} FJD ${String.format("%.2f", tx.amount)} - ${tx.description}")
            }
        }

        if (uiState.selectedAccountId != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = viewModel::loadPreviousPage, enabled = uiState.page > 1) {
                    Text("Previous")
                }
                Text("Page ${uiState.page} / ${uiState.totalPages}")
                OutlinedButton(onClick = viewModel::loadNextPage, enabled = uiState.page < uiState.totalPages) {
                    Text("Next")
                }
            }
        }
    }
}
