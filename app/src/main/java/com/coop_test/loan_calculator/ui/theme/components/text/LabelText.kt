package com.coop_test.loan_calculator.ui.theme.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabelText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
