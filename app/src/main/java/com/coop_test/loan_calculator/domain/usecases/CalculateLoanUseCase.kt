package com.coop_test.loan_calculator.domain.usecases

import com.coop_test.loan_calculator.domain.model.AmortizationScheduleItem
import com.coop_test.loan_calculator.domain.model.LoanCalculation
import kotlin.math.pow

class CalculateLoanUseCase {
    operator fun invoke(
        principal: Double,
        annualRate: Double,
        tenureMonths: Int
    ): Pair<LoanCalculation, List<AmortizationScheduleItem>> {
        val monthlyRate = (annualRate / 100) / 12
        
        val emi = if (monthlyRate > 0) {
            (principal * monthlyRate * (1 + monthlyRate).pow(tenureMonths)) /
                    ((1 + monthlyRate).pow(tenureMonths) - 1)
        } else {
            principal / tenureMonths
        }

        val totalPayment = emi * tenureMonths
        val totalInterest = totalPayment - principal

        val calculation = LoanCalculation(
            principal = principal,
            interestRate = annualRate,
            tenureMonths = tenureMonths,
            monthlyPayment = emi,
            totalInterest = totalInterest,
            totalPayment = totalPayment
        )

        val schedule = mutableListOf<AmortizationScheduleItem>()
        var balance = principal
        
        for (month in 1..tenureMonths) {
            val interestPaid = balance * monthlyRate
            val principalPaid = emi - interestPaid
            val openingBalance = balance
            balance -= principalPaid
            
            schedule.add(
                AmortizationScheduleItem(
                    month = month,
                    openingBalance = openingBalance,
                    monthlyPayment = emi,
                    interestPaid = interestPaid,
                    principalPaid = principalPaid,
                    closingBalance = if (month == tenureMonths) 0.0 else balance
                )
            )
        }

        return Pair(calculation, schedule)
    }
}
