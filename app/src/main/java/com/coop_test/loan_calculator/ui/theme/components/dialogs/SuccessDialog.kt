package com.coop_test.loan_calculator.ui.theme.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    onGoHome: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Request Sent Successfully",
                    color = Color(0xFF8BC34A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF8BC34A),
                    modifier = Modifier.size(100.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Your loan request has been submitted successfully.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = onGoHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Go Home",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
