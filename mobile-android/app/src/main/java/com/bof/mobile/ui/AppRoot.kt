package com.bof.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bof.mobile.data.remote.NetworkModule
import com.bof.mobile.data.repository.AccountRepository
import com.bof.mobile.data.repository.AuthRepository
import com.bof.mobile.data.repository.DashboardRepository
import com.bof.mobile.data.repository.TransferRepository
import com.bof.mobile.ui.accounts.AccountsScreen
import com.bof.mobile.ui.auth.LoginScreen
import com.bof.mobile.ui.auth.RegisterScreen
import com.bof.mobile.ui.dashboard.DashboardScreen
import com.bof.mobile.ui.transfers.TransferScreen
import com.bof.mobile.viewmodel.AccountsViewModel
import com.bof.mobile.viewmodel.AuthViewModel
import com.bof.mobile.viewmodel.DashboardViewModel
import com.bof.mobile.viewmodel.TransferViewModel

private enum class MainTab {
    DASHBOARD,
    ACCOUNTS,
    TRANSFERS
}

@Composable
fun AppRoot() {
    var token by remember { mutableStateOf<String?>(null) }
    val apiService = remember { NetworkModule.createApiService { token } }

    val authViewModel = remember { AuthViewModel(AuthRepository(apiService)) }
    val dashboardViewModel = remember { DashboardViewModel(DashboardRepository(apiService)) }
    val accountsViewModel = remember { AccountsViewModel(AccountRepository(apiService)) }
    val transferViewModel = remember { TransferViewModel(TransferRepository(apiService)) }

    val authState by authViewModel.uiState.collectAsState()
    var showRegister by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(MainTab.DASHBOARD) }

    LaunchedEffect(authState.token) {
        token = authState.token
    }

    if (!authState.isLoggedIn) {
        if (showRegister) {
            RegisterScreen(
                viewModel = authViewModel,
                onBackToLogin = { showRegister = false }
            )
        } else {
            LoginScreen(
                viewModel = authViewModel,
                onOpenRegister = { showRegister = true }
            )
        }
        return
    }

    val customerId = authState.customerId ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Welcome ${authState.fullName}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { activeTab = MainTab.DASHBOARD }) { Text("Dashboard") }
            Button(onClick = { activeTab = MainTab.ACCOUNTS }) { Text("Accounts") }
            Button(onClick = { activeTab = MainTab.TRANSFERS }) { Text("Transfers") }
            Button(onClick = {
                authViewModel.logout()
                showRegister = false
                activeTab = MainTab.DASHBOARD
            }) { Text("Logout") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeTab) {
            MainTab.DASHBOARD -> DashboardScreen(viewModel = dashboardViewModel, customerId = customerId)
            MainTab.ACCOUNTS -> AccountsScreen(viewModel = accountsViewModel)
            MainTab.TRANSFERS -> TransferScreen(viewModel = transferViewModel)
        }
    }
}
