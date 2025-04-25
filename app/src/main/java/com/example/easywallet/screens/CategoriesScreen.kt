package com.example.easywallet.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.AnnotatedString
import com.example.easywallet.db.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(firestore: FirebaseFirestore, context: android.content.Context) {
    // Scroll state for the content
    val scrollState = rememberScrollState()

    // Optimized way for toggle
    val shouldShowTopBarTitle by remember {
        derivedStateOf { scrollState.value >= 115 }
    }

    val topBarColor by remember {
        derivedStateOf {
            if (shouldShowTopBarTitle) Color(0xFFE5E5EA) else Color.Transparent
        }
    }

    // State for bottom sheet
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    // State for text field values
    var categoryName by remember { mutableStateOf("") }
    var categoryLimit by remember { mutableStateOf("") }

    // Content of the sheet (form for adding category)
    val bottomSheetContent = @Composable {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.40f)
                .padding(top = 15.dp, start = 25.dp, end = 25.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = categoryLimit,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) { // Ensure only numbers are allowed
                            categoryLimit = input
                        }
                    },
                    label = { Text("Category Limit") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                onClick = {
                    // Get the current user's UID
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    // Check if the user is authenticated
                    if (userId != null) {
                        // When button is clicked, create a Category object
                        if (categoryName.isNotBlank() && categoryLimit.isNotBlank()) {
                            // Convert categoryLimit to a Double
                            val categoryLimitValue = categoryLimit.toDoubleOrNull()

                            if (categoryLimitValue != null) {
                                // Create the category object, including the user ID
                                val newCategory = Category(
                                    categoryName = categoryName,
                                    categoryLimit = categoryLimitValue,
                                    userId = userId // Link the category to the current user
                                )

                                // Add the category to Firestore
                                firestore.collection("categories") // Firestore collection
                                    .add(newCategory)
                                    .addOnSuccessListener {
                                        // Show success Toast
                                        Toast.makeText(context, "Category added successfully", Toast.LENGTH_SHORT).show()

                                        // Reset fields and close the bottom sheet
                                        categoryName = ""
                                        categoryLimit = ""
                                        showBottomSheet = false
                                    }
                                    .addOnFailureListener { e ->
                                        // Show failure Toast
                                        Toast.makeText(context, "Error adding category: $e", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Show error Toast for invalid category limit
                                Toast.makeText(context, "Invalid category limit", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Show error Toast for missing fields
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Show error Toast if the user is not authenticated
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE5E5EA)
                )
            ) {
                Text(
                    text = "Add Category",
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }

    // Show bottom sheet when clicked
    if (showBottomSheet) {
        coroutineScope.launch {
            sheetState.show() // Trigger to show the bottom sheet
        }
    }

    Scaffold(
        topBar = {
            // Categories TopAppBar
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Edit button
                        ClickableText(
                            text = AnnotatedString("Edit"),
                            onClick = {
                                // Handle the edit button click (e.g., navigate to an edit screen)
                            },
                            style = androidx.compose.ui.text.TextStyle(
                                color = Color(0xFF007AFF),
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(start = 5.dp)
                        )

                        // Only show the "Categories" title when shouldShowTitle is true
                        AnimatedVisibility(
                            visible = shouldShowTopBarTitle,
                            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                            exit = fadeOut(animationSpec = tween(durationMillis = 300))
                        ) {
                            Text(
                                text = "Categories",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Empty space to balance layout
                        Box(modifier = Modifier.size(50.dp))
                    }
                },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor // Change color dynamically
                )
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Main content (e.g., list of categories) that is scrollable
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 20.dp, bottom = innerPadding.calculateBottomPadding() + 120.dp)
                ) {
                    // Header section with title
                    Text(
                        text = "Categories",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Show bottom sheet here (use ModalBottomSheet)
                if (showBottomSheet) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { showBottomSheet = false }
                    ) {
                        bottomSheetContent()
                    }
                }

                // FloatingActionButton (FAB) positioned at the bottom center
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 90.dp)
                        .height(50.dp)
                        .fillMaxWidth(0.80f)
                        .border(1.dp, Color(10, 132, 255), RoundedCornerShape(15)),
                    onClick = {
                        showBottomSheet = true // Show the bottom sheet
                    },
                    containerColor = Color.White
                ) {
                    Text(
                        text = "New Category",
                        fontSize = 19.sp,
                        color = Color(10, 132, 255)
                    )
                }
            }
        }
    )
}
