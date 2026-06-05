package com.coop_test.loan_calculator.data.repos

import com.coop_test.loan_calculator.data.model.LoanCalculation
import com.coop_test.loan_calculator.data.model.LoanOption
import kotlinx.coroutines.flow.Flow

interface LoanRepository {
    suspend fun saveCalculation(calculation: LoanCalculation)
    fun getSavedCalculations(): Flow<List<LoanCalculation>>
    suspend fun deleteCalculation(calculation: LoanCalculation)
    suspend fun getLoanOptions(): List<LoanOption>
}