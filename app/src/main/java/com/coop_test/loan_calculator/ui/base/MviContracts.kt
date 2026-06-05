package com.coop_test.loan_calculator.ui.base

import com.coop_test.loan_calculator.domain.model.AmortizationScheduleItem
import com.coop_test.loan_calculator.domain.model.LoanCalculation
import com.coop_test.loan_calculator.domain.model.LoanOption

interface UiState
interface UiIntent
interface UiEffect

data class LoanState(
    val isLoading: Boolean = false,
    val activeLoan: LoanCalculation? = null,
    val loanCalculation: LoanCalculation? = null,
    val amortizationSchedule: List<AmortizationScheduleItem> = emptyList(),
    val loanOptions: List<LoanOption> = emptyList(),
    val savedCalculations: List<LoanCalculation> = emptyList(),
    val error: String? = null
): UiState

sealed interface LoanIntent : UiIntent {
    data class Calculate(val principal: Double, val rate: Double, val tenure: Int) : LoanIntent
    data class Save(val calculation: LoanCalculation) : LoanIntent
    data class Delete(val calculation: LoanCalculation) : LoanIntent
    object FetchOptions : LoanIntent
    object FetchSaved : LoanIntent
}

sealed interface LoanEffect : UiEffect {
    data class ShowSnackbar(val message: String) : LoanEffect
    object NavigateToSchedule : LoanEffect
}
