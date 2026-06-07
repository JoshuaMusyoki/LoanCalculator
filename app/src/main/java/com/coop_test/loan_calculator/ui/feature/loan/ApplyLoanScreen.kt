package com.coop_test.loan_calculator.ui.feature.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    var expandedPeriod by remember { mutableStateOf(false) }
    var loanLimit by remember { mutableStateOf(12000.00) }
    var expandedLimit by remember { mutableStateOf(false) }
    
    val currentAmount = amount.replace(",", "").toDoubleOrNull() ?: 0.0
    val isError = currentAmount > loanLimit

    LaunchedEffect(amount, period, loanLimit) {
        val cleanAmount = amount.replace(",", "").toDoubleOrNull() ?: 0.0
        val tenure = period.toIntOrNull() ?: 0
        if (cleanAmount > 0 && cleanAmount <= loanLimit && tenure > 0) {
            onIntent(LoanIntent.Calculate(cleanAmount, option.minInterestRate, tenure))
        }
    }

    val calculation = state.loanCalculation
    val schedule = state.amortizationSchedule
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                LabelText("Loan Type")
                ReadOnlyField(option.name, Icons.Default.ArrowDropDown)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(0.6f)) {
                LabelText("Your Limit")
                Box {
                    Box(modifier = Modifier.clickable { expandedLimit = true }) {
                        ReadOnlyField(String.format(Locale.US, "%,.0f", loanLimit), Icons.Default.ArrowDropDown)
                    }
                    DropdownMenu(
                        expanded = expandedLimit,
                        onDismissRequest = { expandedLimit = false }
                    ) {
                        listOf(5000.0, 10000.0, 12000.0, 20000.0, 50000.0).forEach { limit ->
                            DropdownMenuItem(
                                text = { Text(String.format(Locale.US, "%,.0f KES", limit)) },
                                onClick = {
                                    loanLimit = limit
                                    expandedLimit = false
                                }
                            )
                        }
                    }
                }
            }
        }
        
        Text(
            text = "Interest: ${option.minInterestRate}% p.a",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        LabelText("Loan Amount")
        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                amount = newValue
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        "KES",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    VerticalDivider(modifier = Modifier.height(24.dp).width(1.dp))
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
        Text(
            text = if (isError) "Amount exceeds your loan limit of ${String.format(Locale.US, "%,.2f", loanLimit)} KES" 
                  else "Available Loan Limit: ${String.format(Locale.US, "%,.2f", loanLimit)} KES",
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        LabelText("Loan Period (months)")
        Box {
            Box(modifier = Modifier.clickable { expandedPeriod = true }) {
                ReadOnlyField(period, Icons.Default.ArrowDropDown)
            }
            DropdownMenu(
                expanded = expandedPeriod,
                onDismissRequest = { expandedPeriod = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                listOf("1", "2", "3", "6", "12").forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p) },
                        onClick = {
                            period = p
                            expandedPeriod = false
                        }
                    )
                }
            }
        }

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
            items(if (isError) emptyList() else schedule) { item ->
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
            enabled = calculation != null && !isError
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
