package com.coop_test.loan_calculator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coop_test.loan_calculator.domain.model.LoanCalculation

@Entity(tableName = "loan_calculations")
data class LoanCalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val principal: Double,
    val interestRate: Double,
    val tenureMonths: Int,
    val monthlyPayment: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val timestamp: Long
)

fun LoanCalculationEntity.toDomain() = LoanCalculation(
    id = id,
    principal = principal,
    interestRate = interestRate,
    tenureMonths = tenureMonths,
    monthlyPayment = monthlyPayment,
    totalInterest = totalInterest,
    totalPayment = totalPayment,
    timestamp = timestamp
)

fun LoanCalculation.toEntity() = LoanCalculationEntity(
    id = id,
    principal = principal,
    interestRate = interestRate,
    tenureMonths = tenureMonths,
    monthlyPayment = monthlyPayment,
    totalInterest = totalInterest,
    totalPayment = totalPayment,
    timestamp = timestamp
)
