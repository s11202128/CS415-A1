package com.bof.mobile.ui.transfers

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bof.mobile.viewmodel.TransferViewModel

@Composable
fun TransferScreen(viewModel: TransferViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBillers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Transfers", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.fromAccountId,
            onValueChange = viewModel::onFromAccountIdChanged,
            label = { Text("From account id") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.toAccountNumber,
            onValueChange = {
                viewModel.onToAccountNumberChanged(it)
                if (it.length >= 3) {
                    viewModel.searchRecipients(it)
                }
            },
            label = { Text("Destination account number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = viewModel::onAmountChanged,
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.description,
            onValueChange = viewModel::onDescriptionChanged,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = viewModel::validateDestination, modifier = Modifier.fillMaxWidth()) {
            Text("Validate destination")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = viewModel::initiateTransfer, modifier = Modifier.fillMaxWidth()) {
            Text("Initiate transfer")
        }

        if (uiState.isLoading || uiState.isLoadingRecipients) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
        }

        if (!uiState.destinationName.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Recipient: ${uiState.destinationName}")
        }

        if (uiState.requiresOtp) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.otp,
                onValueChange = viewModel::onOtpChanged,
                label = { Text("OTP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = viewModel::verifyTransferOtp, modifier = Modifier.fillMaxWidth()) {
                Text("Verify OTP")
            }
            if (!uiState.debugOtp.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Debug OTP: ${uiState.debugOtp}")
            }
        }

        if (!uiState.errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }

        if (!uiState.successMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(uiState.successMessage ?: "", color = MaterialTheme.colorScheme.primary)
        }

        if (uiState.recipients.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text("Suggested recipients", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(uiState.recipients.take(5)) { recipient ->
                    OutlinedButton(
                        onClick = { viewModel.prefillRecipient(recipient.accountNumber) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("${recipient.accountNumber} - ${recipient.accountHolder}")
                    }
                }
            }
        }

        if (uiState.billers.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text("Billers", style = MaterialTheme.typography.titleMedium)
            uiState.billers.take(5).forEach { biller ->
                Text("${biller.code}: ${biller.name}")
            }
        }
    }
}
