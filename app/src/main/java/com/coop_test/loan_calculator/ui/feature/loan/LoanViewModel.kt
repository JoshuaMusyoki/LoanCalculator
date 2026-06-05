package com.coop_test.loan_calculator.ui.feature.loan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coop_test.loan_calculator.domain.model.LoanCalculation
import com.coop_test.loan_calculator.domain.repository.LoanRepository
import com.coop_test.loan_calculator.domain.usecases.CalculateLoanUseCase
import com.coop_test.loan_calculator.ui.base.LoanEffect
import com.coop_test.loan_calculator.ui.base.LoanIntent
import com.coop_test.loan_calculator.ui.base.LoanState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoanViewModel(
    private val calculateLoanUseCase: CalculateLoanUseCase,
    private val loanRepository: LoanRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoanState())
    val state: StateFlow<LoanState> = _state.asStateFlow()

    private val _effect = Channel<LoanEffect>(Channel.BUFFERED)
    val effect: Flow<LoanEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(LoanIntent.FetchOptions)
        handleIntent(LoanIntent.FetchSaved)
    }

    fun handleIntent(intent: LoanIntent) {
        when (intent) {
            is LoanIntent.Calculate -> calculateLoan(intent.principal, intent.rate, intent.tenure)
            is LoanIntent.Save -> saveCalculation(intent.calculation)
            is LoanIntent.Delete -> deleteCalculation(intent.calculation)
            LoanIntent.FetchOptions -> fetchOptions()
            LoanIntent.FetchSaved -> fetchSavedCalculations()
        }
    }

    private fun calculateLoan(principal: Double, rate: Double, tenure: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val (calculation, schedule) = calculateLoanUseCase(principal, rate, tenure)
                _state.update { 
                    it.copy(
                        isLoading = false,
                        loanCalculation = calculation,
                        amortizationSchedule = schedule
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(LoanEffect.ShowSnackbar(e.message ?: "Calculation failed"))
            }
        }
    }

    private fun saveCalculation(calculation: LoanCalculation) {
        viewModelScope.launch {
            loanRepository.saveCalculation(calculation)
            _effect.send(LoanEffect.ShowSnackbar("Calculation saved successfully"))
        }
    }

    private fun deleteCalculation(calculation: LoanCalculation) {
        viewModelScope.launch {
            loanRepository.deleteCalculation(calculation)
        }
    }

    private fun fetchOptions() {
        viewModelScope.launch {
            val options = loanRepository.getLoanOptions()
            _state.update { it.copy(loanOptions = options) }
        }
    }

    private fun fetchSavedCalculations() {
        loanRepository.getSavedCalculations()
            .onEach { saved ->
                _state.update { it.copy(
                    savedCalculations = saved,
                    activeLoan = saved.firstOrNull() // Demo: first saved is active
                ) }
            }
            .launchIn(viewModelScope)
    }
}
