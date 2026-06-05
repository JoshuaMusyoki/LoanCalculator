package com.coop_test.loan_calculator.ui.feature.loan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.coop_test.loan_calculator.domain.model.LoanOption
import com.coop_test.loan_calculator.ui.base.LoanIntent
import com.coop_test.loan_calculator.ui.base.LoanState
import com.coop_test.loan_calculator.ui.theme.TestAppTheme
import com.coop_test.loan_calculator.ui.theme.components.text.LabelText
import com.coop_test.loan_calculator.ui.theme.components.textfields.ReadOnlyField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ApplyLoanScreen(
    option: LoanOption,
    state: LoanState,
    onIntent: (LoanIntent) -> Unit,
    onApply: () -> Unit
) {
    var amount by remember { mutableStateOf("10,000.00") }
    var period by remember { mutableStateOf("2") }
    val loanLimit = 12000.00

    LaunchedEffect(amount, period) {
        val cleanAmount = amount.replace(",", "").toDoubleOrNull() ?: 0.0
        val tenure = period.toIntOrNull() ?: 0
        if (cleanAmount > 0 && tenure > 0) {
            onIntent(LoanIntent.Calculate(cleanAmount, option.minInterestRate, tenure))
        }
    }

    val calculation = state.loanCalculation
    val schedule = state.amortizationSchedule

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LabelText("Loan Type")
        ReadOnlyField(option.name, Icons.Default.ArrowDropDown)
        Text(
            text = "Interest: ${option.minInterestRate}% p.a",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        LabelText("Loan Amount")
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        "KES",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    VerticalDivider(modifier = Modifier.height(24.dp).width(1.dp))
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
        Text(
            text = "Available Loan Limit: ${String.format(Locale.US, "%,.2f", loanLimit)} KES",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        LabelText("Loan Period (months)")
        ReadOnlyField(period, Icons.Default.ArrowDropDown)

        Text(
            text = "Total Amount Payable: ${String.format(Locale.US, "%,.2f", calculation?.totalPayment ?: 0.0)} KES",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        LabelText("Disbursement Account")
        ReadOnlyField("011090145246100", Icons.Default.ArrowDropDown)
        Text(
            text = "Available Loan Limit: ${String.format(Locale.US, "%,.2f", loanLimit)} KES",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Repayment Schedule",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(schedule) { item ->
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
                val installmentDate = sdf.format(calendar.apply { add(Calendar.MONTH, item.month) }.time)
                
                RepaymentItem(
                    label = "${getOrdinal(item.month)} instalment - $installmentDate",
                    value = "${String.format(Locale.US, "%,.2f", item.monthlyPayment)} KES"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp),
            enabled = calculation != null
        ) {
            Text(
                "Apply Loan",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun getOrdinal(n: Int): String {
    val suffixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
    return when (n % 100) {
        11, 12, 13 -> n.toString() + "th"
        else -> n.toString() + suffixes[n % 10]
    }
}

@PreviewLightDark
@Composable
fun ApplyLoanScreenPreview() {
    TestAppTheme(dynamicColor = false) {
        val sampleOption = LoanOption(
            id = "1",
            name = "Salary E-Loan",
            description = "Get quick loans to boost your income",
            minInterestRate = 15.57,
            maxTenureMonths = 12
        )
        ApplyLoanScreen(
            option = sampleOption,
            state = LoanState(
                loanCalculation = com.coop_test.loan_calculator.domain.model.LoanCalculation(
                    principal = 10000.0,
                    interestRate = 15.57,
                    tenureMonths = 2,
                    monthlyPayment = 5750.0,
                    totalInterest = 1500.0,
                    totalPayment = 11500.0
                ),
                amortizationSchedule = listOf(
                    com.coop_test.loan_calculator.domain.model.AmortizationScheduleItem(1, 10000.0, 5750.0, 750.0, 5000.0, 5000.0),
                    com.coop_test.loan_calculator.domain.model.AmortizationScheduleItem(2, 5000.0, 5750.0, 750.0, 5000.0, 0.0)
                )
            ),
            onIntent = {},
            onApply = {}
        )
    }
}
