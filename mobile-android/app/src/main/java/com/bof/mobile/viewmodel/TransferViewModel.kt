package com.bof.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bof.mobile.data.repository.TransferRepository
import com.bof.mobile.model.ApiResult
import com.bof.mobile.model.BillerItem
import com.bof.mobile.model.RecipientItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransferUiState(
    val fromAccountId: String = "",
    val toAccountNumber: String = "",
    val amount: String = "",
    val description: String = "",
    val otp: String = "",
    val destinationName: String? = null,
    val transferId: String? = null,
    val requiresOtp: Boolean = false,
    val debugOtp: String? = null,
    val isLoading: Boolean = false,
    val isLoadingRecipients: Boolean = false,
    val recipients: List<RecipientItem> = emptyList(),
    val billers: List<BillerItem> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class TransferViewModel(private val transferRepository: TransferRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState

    fun onFromAccountIdChanged(value: String) = _uiState.update { it.copy(fromAccountId = value) }
    fun onToAccountNumberChanged(value: String) = _uiState.update { it.copy(toAccountNumber = value) }
    fun onAmountChanged(value: String) = _uiState.update { it.copy(amount = value) }
    fun onDescriptionChanged(value: String) = _uiState.update { it.copy(description = value) }
    fun onOtpChanged(value: String) = _uiState.update { it.copy(otp = value) }

    fun clearMessages() = _uiState.update { it.copy(errorMessage = null, successMessage = null) }

    fun searchRecipients(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(recipients = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRecipients = true, errorMessage = null) }
            when (val result = transferRepository.searchRecipients(query.trim())) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingRecipients = false,
                            recipients = result.data,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingRecipients = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun loadBillers() {
        viewModelScope.launch {
            when (val result = transferRepository.getBillers()) {
                is ApiResult.Success -> _uiState.update { it.copy(billers = result.data) }
                is ApiResult.Error -> _uiState.update { it.copy(errorMessage = result.message) }
            }
        }
    }

    fun validateDestination() {
        val state = _uiState.value
        val fromAccountId = state.fromAccountId.toIntOrNull()
        if (fromAccountId == null || state.toAccountNumber.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Provide source account id and destination account number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            when (
                val result = transferRepository.validateDestination(
                    fromAccountId = fromAccountId,
                    toAccountNumber = state.toAccountNumber.trim()
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            destinationName = result.data.customerName,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            destinationName = null,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun initiateTransfer() {
        val state = _uiState.value
        val fromAccountId = state.fromAccountId.toIntOrNull()
        val amountValue = state.amount.toDoubleOrNull()

        if (fromAccountId == null || state.toAccountNumber.isBlank() || amountValue == null || amountValue <= 0.0) {
            _uiState.update {
                it.copy(errorMessage = "Enter valid source account, destination account, and amount")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            when (
                val result = transferRepository.initiateTransfer(
                    fromAccountId = fromAccountId,
                    toAccountNumber = state.toAccountNumber.trim(),
                    amount = amountValue,
                    description = state.description.trim()
                )
            ) {
                is ApiResult.Success -> {
                    val response = result.data
                    val successMessage = if (response.requiresOtp) {
                        "OTP sent. Enter OTP to complete transfer."
                    } else {
                        "Transfer completed successfully"
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            requiresOtp = response.requiresOtp,
                            transferId = response.transferId,
                            debugOtp = response.otp,
                            successMessage = successMessage,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun verifyTransferOtp() {
        val state = _uiState.value
        val transferId = state.transferId
        if (transferId.isNullOrBlank() || state.otp.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Transfer id and OTP are required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            when (val result = transferRepository.verifyTransfer(transferId = transferId, otp = state.otp.trim())) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            requiresOtp = false,
                            otp = "",
                            successMessage = "Transfer verified successfully",
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun prefillRecipient(accountNumber: String) {
        _uiState.update { it.copy(toAccountNumber = accountNumber) }
    }
}
