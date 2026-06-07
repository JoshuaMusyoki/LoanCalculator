package com.coop_test.loan_calculator.ui.feature.loan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coop_test.loan_calculator.domain.model.AmortizationScheduleItem
import com.coop_test.loan_calculator.domain.model.LoanCalculation
import com.coop_test.loan_calculator.ui.base.LoanState
import com.coop_test.loan_calculator.ui.theme.TestAppTheme
import java.util.Locale

@Composable
fun ScheduleScreen(
    viewModel: LoanViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ScheduleContent(
        state = state,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleContent(
    state: LoanState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Amortization Schedules") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.savedCalculations.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("No saved loans found")
                    }
                }
            } else {
                state.savedCalculations.forEach { loan ->
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Loan: Salary E-Loan",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Principal: ${String.format(Locale.US, "%,.2f", loan.principal)} KES",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    item {
                        ScheduleHeader()
                    }

                    val schedule = state.savedSchedules[loan.id] ?: emptyList()
                    items(schedule) { item ->
                        ScheduleRow(
                            month = item.month,
                            emi = item.monthlyPayment,
                            principal = item.principalPaid,
                            interest = item.interestPaid,
                            balance = item.closingBalance
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("No.", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
        Text("EMI", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text("Principal", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text("Interest", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text("Balance", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.8f))
    }
}

@Composable
fun ScheduleRow(
    month: Int,
    emi: Double,
    principal: Double,
    interest: Double,
    balance: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(month.toString(), modifier = Modifier.weight(0.7f))
        Text(String.format(Locale.US, "%,.0f", emi), modifier = Modifier.weight(1.5f))
        Text(String.format(Locale.US, "%,.0f", principal), modifier = Modifier.weight(1.5f))
        Text(String.format(Locale.US, "%,.0f", interest), modifier = Modifier.weight(1.5f))
        Text(String.format(Locale.US, "%,.0f", balance), modifier = Modifier.weight(1.8f))
    }
}

@PreviewLightDark
@Composable
fun ScheduleScreenPreview() {
    TestAppTheme {
        Surface {
            ScheduleContent(
                state = LoanState(
                    savedCalculations = listOf(
                        LoanCalculation(
                            id = 1,
                            principal = 200000.0,
                            interestRate = 12.0,
                            tenureMonths = 5,
                            monthlyPayment = 41208.0,
                            totalInterest = 6040.0,
                            totalPayment = 206040.0
                        )
                    ),
                    savedSchedules = mapOf(
                        1 to listOf(
                            AmortizationScheduleItem(1, 200000.0, 41208.0, 2000.0, 39208.0, 160792.0),
                            AmortizationScheduleItem(2, 160792.0, 41208.0, 1608.0, 39600.0, 121192.0)
                        )
                    )
                ),
                onBack = {}
            )
        }
    }
}
