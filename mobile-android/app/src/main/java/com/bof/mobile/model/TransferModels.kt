package com.bof.mobile.model

data class ValidateDestinationRequest(
    val fromAccountId: Int,
    val toAccountNumber: String
)

data class ValidateDestinationResponse(
    val accountNumber: String,
    val customerId: Int,
    val customerName: String
)

data class InitiateTransferRequest(
    val fromAccountId: Int,
    val toAccountNumber: String,
    val amount: Double,
    val description: String
)

data class InitiateTransferResponse(
    val highValueThreshold: Double,
    val status: String,
    val requiresOtp: Boolean,
    val transferId: String?,
    val otp: String?
)

data class VerifyTransferRequest(
    val transferId: String,
    val otp: String
)

data class VerifyTransferResponse(
    val status: String,
    val transferId: String?
)
