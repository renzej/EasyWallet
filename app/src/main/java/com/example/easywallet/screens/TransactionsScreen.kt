package com.example.easywallet.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.navigation.NavHostController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.zIndex
import com.example.easywallet.db.Category
import com.example.easywallet.db.Transactions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(navController: NavHostController) {
    var showBottomCard by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var transactions by remember { mutableStateOf<List<Transactions>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch categories
    LaunchedEffect(userId) {
        if (userId != null) {
            fetchCategories(userId, { categories = it }, { isLoading = it })
            fetchTransactions(userId, { transactions = it })
        }
    }

    val offsetY by animateDpAsState(
        targetValue = if (showBottomCard) 0.dp else 1000.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    // Use a scrollable column for everything (header included)
    Column(modifier = Modifier.fillMaxSize()) {
        // Header that scrolls away with content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                IconButton(onClick = { /* TODO: Edit logic */ }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { showBottomCard = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        }

        // Transaction List
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            if (transactions.isEmpty()) {
                Text("No transactions yet.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    transactions.forEach { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }

    // Bottom sheet card (Form)
    AnimatedVisibility(
        visible = showBottomCard,
        enter = slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(500)),
        exit = slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = offsetY)
                .zIndex(1f),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                TransactionForm(
                    categories = categories,
                    onExit = { showBottomCard = false },
                    onSave = { category, amount, date ->
                        showBottomCard = false

                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            val db = FirebaseFirestore.getInstance()
                            val transaction = hashMapOf(
                                "userId" to userId,
                                "category" to category,
                                "amount" to amount,
                                "date" to date
                            )

                            db.collection("transactions")
                                .add(transaction)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Transaction saved", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to save transaction", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionForm(
    categories: List<Category>,
    onExit: () -> Unit,
    onSave: (String, Double, String) -> Unit  // Updated to pass Double instead of String
) {
    var selectedCategory by remember { mutableStateOf("Select Category") }
    var amount by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val currentDate = dateFormatter.format(Date())
    var selectedDate by remember { mutableStateOf(currentDate) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top Exit and Save Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { onExit() }) {
                Text("Exit", fontSize = 17.sp, color = Color.Gray)
            }
            TextButton(
                onClick = {
                    if (selectedCategory != "Select Category" && amount.isNotBlank()) {
                        val amountDouble = amount.toDoubleOrNull() ?: 0.0  // Convert amount to Double
                        onSave(selectedCategory, amountDouble, selectedDate)
                    } else {
                        Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Save", fontSize = 17.sp)
            }
        }

        // Main Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CategoryDropdown(
                categoryOptions = categories.map { it.categoryName },
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount Spent") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Date: $selectedDate")
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    categoryOptions: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Category:",
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal
        )

        Box {
            TextButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .defaultMinSize(minWidth = 1.dp)
            ) {
                Text(text = selectedCategory, fontSize = 17.sp)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                categoryOptions.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category, fontSize = 15.sp) },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Fetch Categories function
fun fetchCategories(userId: String?, updateCategories: (List<Category>) -> Unit, updateLoading: (Boolean) -> Unit) {
    if (userId != null) {
        FirebaseFirestore.getInstance().collection("categories")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val fetchedCategories = snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
                updateCategories(fetchedCategories)  // Update categories state
                updateLoading(false)  // Set loading state to false
            }
            .addOnFailureListener {
                updateCategories(emptyList())  // Return empty list on failure
                updateLoading(false)  // Set loading state to false
            }
    }
}

fun fetchTransactions(userId: String?, updateTransactions: (List<Transactions>) -> Unit) {
    if (userId != null) {
        FirebaseFirestore.getInstance().collection("transactions")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val fetchedTransactions = snapshot.documents.mapNotNull { it.toObject(Transactions::class.java) }
                updateTransactions(fetchedTransactions)
            }
            .addOnFailureListener {
                updateTransactions(emptyList()) // return empty list on failure
            }
    }
}

@Composable
fun TransactionItem(transaction: Transactions) {
    val formattedAmount = "%.2f".format(transaction.amount) // Format amount to 2 decimal places

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Category: ${transaction.category}", fontWeight = FontWeight.Bold)
            Text(text = "Amount: \$${formattedAmount}") // Display the formatted amount
            Text(text = "Date: ${transaction.date}")
        }
    }
}


