package com.example.easywallet.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {
    val scrollState = rememberScrollState()

    // Track scroll position for animations
    val shouldShowTopBarTitle by remember {
        derivedStateOf { scrollState.value >= 95 }
    }

    // Dynamic color transition for TopAppBar
    val topBarColor by remember {
        derivedStateOf {
            if (shouldShowTopBarTitle) Color(0xFFE5E5EA) else Color.Transparent
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = shouldShowTopBarTitle,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300))
                    ) {
                        Text(
                            text = "Account",
                            modifier = Modifier.padding(top = 20.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, bottom = innerPadding.calculateBottomPadding() + 40.dp)
                .verticalScroll(scrollState)
        ) {
            // **Header Section**
            Text(
                text = "Account",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )

        }
    }
}
