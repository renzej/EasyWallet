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
fun NewsScreen() {
    val scrollState = rememberScrollState()

    // Track when to show the top bar title and change color
    val shouldShowTopBarTitle by remember {
        derivedStateOf { scrollState.value >= 95 }
    }
    // Dynamic top bar color change
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
                            text = "News",
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
                text = "News",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )

            // Dummy content to allow scrolling
            repeat(30) {
                Text(
                    text = "News Article $it",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
                )
            }
        }
    }
}
