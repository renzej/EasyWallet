package com.example.easywallet.screens

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.navigation.NavHostController
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UseOfNonLambdaOffsetOverload")
@Composable
fun TransactionsScreen(navController: NavHostController) {
    var showBottomCard by remember { mutableStateOf(false) }

    // Animate the position from the bottom
    val offsetY by animateDpAsState(
        targetValue = if (showBottomCard) 0.dp else 1000.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Edit button as text, wrapped in Box for handling clickability
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .then(if (showBottomCard) Modifier.pointerInput(Unit) { /* Block Clicks */ } else Modifier)
                        ) {
                            ClickableText(
                                text = AnnotatedString("Edit"),
                                onClick = {
                                    if (!showBottomCard) {
                                        // Handle edit click
                                    }
                                },
                                style = TextStyle(
                                    color = if (showBottomCard) Color.Gray else Color(0xFF007AFF),
                                    fontSize = 18.sp
                                )
                            )
                        }

                        Text(
                            text = "Transaction",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Add button
                        IconButton(
                            onClick = { if (!showBottomCard) showBottomCard = true },
                            enabled = !showBottomCard // Disable when bottom card is visible
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Transaction",
                                tint = if (showBottomCard) Color.Gray else Color(0xFF007AFF),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.height(80.dp)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Main content (Transactions list)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Example Transactions List (Replace with real data)
                    repeat(10) {
                        Text(
                            text = "Transaction Item $it",
                            modifier = Modifier.padding(5.dp),
                            fontSize = 16.sp
                        )
                    }
                }

                // Custom Card acting as Modal Bottom Sheet
                AnimatedVisibility(
                    visible = showBottomCard,
                    enter = slideInVertically(
                        initialOffsetY = { 1000 },
                        animationSpec = tween(durationMillis = 500)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { 1000 },
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth() // Remove padding for full width
                            .fillMaxHeight()
                            .offset(y = offsetY) // Animate the offset
                            .padding(horizontal = 0.dp, vertical = 16.dp)
                            .zIndex(1f),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // "Exit" clickable text on the left
                                ClickableText(
                                    text = AnnotatedString("Exit"),
                                    onClick = {
                                        // Hide the card with animation when "Exit" is clicked
                                        showBottomCard = false
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF007AFF),
                                        fontSize = 18.sp
                                    )
                                )

                                // "Save" clickable text on the right
                                ClickableText(
                                    text = AnnotatedString("Save"),
                                    onClick = {
                                        // Handle save action
                                        // For example, save data or submit form
                                    },
                                    style = TextStyle(
                                        color = Color(0xFF007AFF),
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
