package com.bof.mobile.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bof.mobile.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, customerId: Int) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(customerId) {
        viewModel.loadDashboard(customerId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
            return@Column
        }

        if (!uiState.errorMessage.isNullOrBlank()) {
            Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.loadDashboard(customerId) }) {
                Text("Retry")
            }
            return@Column
        }

        val data = uiState.data
        if (data == null) {
            Text("No dashboard data available")
            return@Column
        }

        val totalBalance = data.accounts.sumOf { it.balance }
        val activeAccounts = data.accounts.count { it.status.equals("active", ignoreCase = true) }

        Text("Welcome, ${data.customer.fullName}")
        Text("Total Balance: FJD ${String.format(\"%.2f\", totalBalance)}")
        Text("Active Accounts: $activeAccounts")

        Spacer(modifier = Modifier.height(16.dp))
        Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(data.recentTransactions) { tx ->
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Text("${tx.type.uppercase()} FJD ${String.format(\"%.2f\", tx.amount)}")
                    Text(tx.description, style = MaterialTheme.typography.bodySmall)
                    Text("Account ${tx.accountNumber}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
