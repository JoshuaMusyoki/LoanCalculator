package com.coop_test.loan_calculator.ui.theme.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ApplyLoanHeader(
    title: String,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF003D33))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}