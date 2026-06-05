package com.coop_test.loan_calculator.domain.repository

import com.coop_test.loan_calculator.domain.model.LoanCalculation
import com.coop_test.loan_calculator.domain.model.LoanOption
import kotlinx.coroutines.flow.Flow

interface LoanRepository {
    suspend fun saveCalculation(calculation: LoanCalculation)
    fun getSavedCalculations(): Flow<List<LoanCalculation>>
    suspend fun deleteCalculation(calculation: LoanCalculation)
    suspend fun getLoanOptions(): List<LoanOption>
}
