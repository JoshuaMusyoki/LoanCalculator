package com.coop_test.loan_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.coop_test.loan_calculator.ui.feature.loan.LoanScreen
import com.coop_test.loan_calculator.ui.feature.loan.ScheduleScreen
import com.coop_test.loan_calculator.ui.feature.loan.LoanViewModel
import com.coop_test.loan_calculator.ui.theme.TestAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

enum class Screen {
    Home, Schedule
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                val viewModel: LoanViewModel = koinViewModel()
                var currentScreen by remember { mutableStateOf(Screen.Home) }
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Crossfade(targetState = currentScreen, label = "") { screen ->
                        when (screen) {
                            Screen.Home -> LoanScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = viewModel,
                                onNavigateToSchedule = { currentScreen = Screen.Schedule },
                                showSnackbar = { message ->
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            )
                            Screen.Schedule -> ScheduleScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                    }
                }
            }
        }
    }
}
