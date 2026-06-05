package com.coop_test.loan_calculator.ui.theme.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coop_test.loan_calculator.domain.model.LoanOption

@Composable
fun AvailableLoanCard(option: LoanOption, onApplyClick: (LoanOption) -> Unit) {
    val gradient = when (option.id) {
        "1" -> Brush.horizontalGradient(colors = listOf(Color(0xFF1B5E20), Color(0xFF4CAF50)))
        "2" -> Brush.horizontalGradient(colors = listOf(Color(0xFF01579B), Color(0xFF039BE5)))
        else -> Brush.horizontalGradient(colors = listOf(Color(0xFFBF360C), Color(0xFFE64A19)))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.background(gradient).fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = option.name,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = option.description,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            lineHeight = 18.sp
                        )
                    }

                    Surface(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp),
                        onClick = { onApplyClick(option) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Apply Now", color = Color.White, fontSize = 12.sp)
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterVertically)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (option.id) {
                        "1" -> Icons.Outlined.Person
                        "2" -> Icons.Outlined.ShoppingCart
                        else -> Icons.Outlined.Inventory
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}