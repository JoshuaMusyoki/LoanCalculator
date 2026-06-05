package com.coop_test.loan_calculator.ui.theme.components.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmationSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
        content()
    }
}