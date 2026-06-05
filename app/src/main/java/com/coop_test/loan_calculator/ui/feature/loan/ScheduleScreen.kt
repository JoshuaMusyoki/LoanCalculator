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
import com.coop_test.loan_calculator.ui.theme.TestAppTheme
import java.util.Locale

@Composable
fun ScheduleScreen(
    viewModel: LoanViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ScheduleContent(
        amortizationSchedule = state.amortizationSchedule,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleContent(
    amortizationSchedule: List<AmortizationScheduleItem>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Amortization Schedule") },
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
            item {
                ScheduleHeader()
            }
            items(amortizationSchedule) { item ->
                ScheduleRow(
                    month = item.month,
                    principal = item.principalPaid,
                    interest = item.interestPaid,
                    balance = item.closingBalance
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
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
        Text("Month", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text("Principal", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
        Text("Interest", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
        Text("Balance", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
    }
}

@Composable
fun ScheduleRow(
    month: Int,
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
        Text(month.toString(), modifier = Modifier.weight(1f))
        Text(String.format(Locale.US, "%,.0f", principal), modifier = Modifier.weight(2f))
        Text(String.format(Locale.US, "%,.0f", interest), modifier = Modifier.weight(2f))
        Text(String.format(Locale.US, "%,.0f", balance), modifier = Modifier.weight(2f))
    }
}

@PreviewLightDark
@Composable
fun ScheduleScreenPreview() {
    TestAppTheme {
        Surface {
            ScheduleContent(
                amortizationSchedule = listOf(
                    AmortizationScheduleItem(1, 200000.0, 20000.0, 5000.0, 15000.0, 185000.0),
                    AmortizationScheduleItem(2, 185000.0, 20000.0, 4800.0, 15200.0, 169800.0),
                    AmortizationScheduleItem(3, 169800.0, 20000.0, 4600.0, 15400.0, 154400.0),
                    AmortizationScheduleItem(4, 154400.0, 20000.0, 4400.0, 15600.0, 138800.0),
                    AmortizationScheduleItem(5, 138800.0, 20000.0, 4200.0, 15800.0, 123000.0)
                ),
                onBack = {}
            )
        }
    }
}