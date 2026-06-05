package com.coop_test.loan_calculator.data.repos

import com.coop_test.loan_calculator.data.local.LoanDao
import com.coop_test.loan_calculator.data.local.toDomain
import com.coop_test.loan_calculator.data.local.toEntity
import com.coop_test.loan_calculator.domain.model.LoanCalculation
import com.coop_test.loan_calculator.domain.model.LoanOption
import com.coop_test.loan_calculator.domain.repository.LoanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoanRepositoryImpl(
    private val loanDao: LoanDao
) : LoanRepository {
    
    override suspend fun saveCalculation(calculation: LoanCalculation) {
        loanDao.insertCalculation(calculation.toEntity())
    }

    override fun getSavedCalculations(): Flow<List<LoanCalculation>> {
        return loanDao.getAllCalculations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteCalculation(calculation: LoanCalculation) {
        loanDao.deleteCalculation(calculation.toEntity())
    }

    override suspend fun getLoanOptions(): List<LoanOption> {
        return listOf(
            LoanOption("1", "Salary E-Loan", "Get quick loans to boost your income", 15.57, 12),
            LoanOption("2", "Buy Now Pay Later", "Buy goods today, pay later", 15.0, 6),
            LoanOption("3", "Stock Loan", "Boost your business stock today", 10.0, 24)
        )
    }
}
