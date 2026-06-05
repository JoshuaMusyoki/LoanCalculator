package com.coop_test.loan_calculator.domain.model

data class TransferReceipt(
    val transactionId: String,
    val isSuccess: Boolean,
    val processedAt: String,
    val errorMessage: String?
)