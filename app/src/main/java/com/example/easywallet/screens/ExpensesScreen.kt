package com.example.easywallet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ExpensesScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .padding(vertical = 22.dp, horizontal = 30.dp)
            .fillMaxSize()
    ) {
        // Category Header Section
        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Category Header
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                text = "Category"
            )
            // Button to navigate to Category Screen
            Button(
                modifier = Modifier
                    .height(35.dp), // Ensure sufficient height
                contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64D2FF).copy(alpha = 0.2f)),
                onClick = { navController.navigate("categories") }
            ) {
                Text(
                    text = "MORE",
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF007AFF)
                )
            }
        }
    }
}
