package com.coop_test.loan_calculator.data.model

data class LoanCalculation(
    val id: Int = 0,
    val principal: Double,
    val interestRate: Double, // Annual interest rate in percentage
    val tenureMonths: Int,
    val monthlyPayment: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class AmortizationScheduleItem(
    val month: Int,
    val openingBalance: Double,
    val monthlyPayment: Double,
    val interestPaid: Double,
    val principalPaid: Double,
    val closingBalance: Double
)

data class LoanOption(
    val id: String,
    val name: String,
    val description: String,
    val minInterestRate: Double,
    val maxTenureMonths: Int
)
