package com.coop_test.loan_calculator.ui.feature.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coop_test.loan_calculator.domain.model.LoanOption
import com.coop_test.loan_calculator.ui.base.LoanState
import com.coop_test.loan_calculator.ui.theme.components.sections.ConfirmationSection
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun LoanConfirmationScreen(
    option: LoanOption,
    state: LoanState,
    onConfirm: () -> Unit
) {
    val calculation = state.loanCalculation
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
    val nextDate = sdf.format(calendar.apply { add(Calendar.MONTH, 1) }.time)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ConfirmationSection("Loan Details") {
            ConfirmationRow("Loan Amount:", "${String.format(Locale.US, "%,.2f", calculation?.principal ?: 10000.0)} KES", isGreen = true)
            ConfirmationRow("Interest:", "${String.format(Locale.US, "%,.2f", calculation?.totalInterest ?: 1500.0)} KES")
            ConfirmationRow("Total Charges:", "${String.format(Locale.US, "%,.2f", calculation?.totalPayment ?: 11500.0)} KES")
            ConfirmationRow("Period:", "${calculation?.tenureMonths ?: 2} Months")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        ConfirmationSection("Disbursement Details") {
            ConfirmationRow("Account:", "011090145246100")
            ConfirmationRow("Amount:", "${String.format(Locale.US, "%,.2f", calculation?.principal ?: 10000.0)} KES")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        ConfirmationSection("Repayment Details") {
            ConfirmationRow("Amount:", "${String.format(Locale.US, "%,.2f", calculation?.totalPayment ?: 11500.0)} KES", isGreen = true)
            ConfirmationRow("Installments:", "${calculation?.tenureMonths ?: 2}")
            ConfirmationRow("Next Repayment Date:", nextDate)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF689F38)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Confirm", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}