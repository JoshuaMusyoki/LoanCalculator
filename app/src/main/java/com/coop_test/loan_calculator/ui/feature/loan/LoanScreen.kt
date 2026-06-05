package com.coop_test.loan_calculator.ui.feature.loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coop_test.loan_calculator.domain.model.LoanOption
import com.coop_test.loan_calculator.ui.base.LoanEffect
import com.coop_test.loan_calculator.ui.base.LoanIntent
import com.coop_test.loan_calculator.ui.base.LoanState
import com.coop_test.loan_calculator.ui.theme.TestAppTheme
import com.coop_test.loan_calculator.ui.theme.components.cards.ActiveLoanCard
import com.coop_test.loan_calculator.ui.theme.components.cards.AvailableLoanCard
import com.coop_test.loan_calculator.ui.theme.components.dialogs.SuccessDialog
import com.coop_test.loan_calculator.ui.theme.components.text.ApplyLoanHeader
import com.coop_test.loan_calculator.ui.theme.components.text.LoanHeader
import com.coop_test.loan_calculator.ui.theme.components.text.SectionTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel

enum class LoanStep {
    INPUT, CONFIRMATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanScreen(
    modifier: Modifier = Modifier,
    viewModel: LoanViewModel = koinViewModel(),
    onNavigateToSchedule: () -> Unit,
    showSnackbar: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoanScreenContent(
        modifier = modifier,
        state = state,
        effect = viewModel.effect,
        onIntent = viewModel::handleIntent,
        onNavigateToSchedule = onNavigateToSchedule,
        showSnackbar = showSnackbar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanScreenContent(
    modifier: Modifier = Modifier,
    state: LoanState,
    effect: Flow<LoanEffect>,
    onIntent: (LoanIntent) -> Unit,
    onNavigateToSchedule: () -> Unit,
    showSnackbar: (String) -> Unit
) {
    var showCalculator by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<LoanOption?>(null) }
    var currentStep by remember { mutableStateOf(LoanStep.INPUT) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(key1 = true) {
        effect.collectLatest { effect ->
            when (effect) {
                is LoanEffect.ShowSnackbar -> showSnackbar(effect.message)
                LoanEffect.NavigateToSchedule -> {
                    showCalculator = false
                    onNavigateToSchedule()
                }
            }
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = { showSuccessDialog = false },
            onGoHome = {
                showSuccessDialog = false
                showCalculator = false
                currentStep = LoanStep.INPUT
            }
        )
    }

    if (showCalculator && selectedOption != null) {
        ModalBottomSheet(
            onDismissRequest = { 
                showCalculator = false 
                currentStep = LoanStep.INPUT
            },
            sheetState = sheetState,
            dragHandle = null,
            containerColor = Color(0xFF003D33),
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ApplyLoanHeader(
                    title = if (currentStep == LoanStep.INPUT) "Apply Loan" else "Confirm Loan",
                    onBack = {
                        if (currentStep == LoanStep.CONFIRMATION) {
                            currentStep = LoanStep.INPUT
                        } else {
                            showCalculator = false
                        }
                    },
                    onClose = {
                        showCalculator = false
                        currentStep = LoanStep.INPUT
                    }
                )
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    when (currentStep) {
                        LoanStep.INPUT -> {
                            ApplyLoanScreen(
                                option = selectedOption!!,
                                state = state,
                                onIntent = onIntent,
                                onApply = { currentStep = LoanStep.CONFIRMATION }
                            )
                        }
                        LoanStep.CONFIRMATION -> {
                            LoanConfirmationScreen(
                                option = selectedOption!!,
                                state = state,
                                onConfirm = {
                                    state.loanCalculation?.let { 
                                        onIntent(LoanIntent.Save(it))
                                        showSuccessDialog = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = { LoanHeader() }
    ) { padding ->
        LoanContent(
            modifier = modifier.padding(padding),
            state = state,
            onIntent = onIntent,
            onApplyClick = { option ->
                selectedOption = option
                showCalculator = true
            }
        )
    }
}



@Composable
fun LoanContent(
    modifier: Modifier = Modifier,
    state: LoanState,
    onIntent: (LoanIntent) -> Unit,
    onApplyClick: (LoanOption) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.activeLoan != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("Active Loans")
                ActiveLoanCard(state.activeLoan)
            }
            item {
                SectionTitle("Other Loans Available")
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("Available Loans")
            }
        }

        items(state.loanOptions) { option ->
            if (state.activeLoan == null || option.name != "Salary E-Loan") {
                AvailableLoanCard(option, onApplyClick)
            }
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun LoanDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
@Composable
fun RepaymentItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ConfirmationRow(label: String, value: String, isGreen: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (isGreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@PreviewLightDark
@Composable
fun LoanScreenPreview() {
    TestAppTheme {
        val dummyOptions = listOf(
            LoanOption("1", "Salary E-Loan", "Get quick loans to boost your income", 12.0, 12),
            LoanOption("2", "Buy Now Pay Later", "Buy goods today, pay later", 15.0, 6)
        )
        LoanScreenContent(
            state = LoanState(loanOptions = dummyOptions),
            effect = emptyFlow(),
            onIntent = {},
            onNavigateToSchedule = {},
            showSnackbar = {}
        )
    }
}
