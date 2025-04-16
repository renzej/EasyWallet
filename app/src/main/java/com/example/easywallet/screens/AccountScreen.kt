package com.example.easywallet.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easywallet.SignInActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {
    val scrollState = rememberScrollState()

    // Track scroll position for animations
    val shouldShowTopBarTitle by remember {
        derivedStateOf { scrollState.value >= 95 }
    }

    // Firebase authentication instance
    val auth = FirebaseAuth.getInstance()

    // Handle logout
    val context = LocalContext.current

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
                .padding(start = 10.dp, end = 10.dp, bottom = innerPadding.calculateBottomPadding() + 40.dp)
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

            // Logout Section
            Spacer(modifier = Modifier.height(20.dp)) // Space before button
            LogOutButton(auth, context)
        }
    }
}

@Composable
fun LogOutButton(
    auth: FirebaseAuth,
    context: Context
) {
    Button(
        onClick =
        {
            auth.signOut() // Sign out the user
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate to SignInActivity and clear the back stack
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Logout", color = Color.White)
    }
}


