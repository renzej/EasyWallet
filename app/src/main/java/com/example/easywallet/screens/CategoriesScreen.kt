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
import androidx.compose.ui.text.TextStyle
import com.example.easywallet.db.Category
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(firestore: FirebaseFirestore, context: android.content.Context) {
    val scrollState = rememberScrollState()
    val shouldShowTopBarTitle by remember {
        derivedStateOf { scrollState.value >= 115 }
    }
    val topBarColor by remember {
        derivedStateOf {
            if (shouldShowTopBarTitle) Color(0xFFE5E5EA) else Color.Transparent
        }
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    fun fetchCategories() {
        if (userId != null) {
            firestore.collection("categories")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    categories = snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
                    isLoading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error fetching categories", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
        }
    }

    // Initial load
    LaunchedEffect(userId) {
        fetchCategories()
    }

    // Refresh state
    val isRefreshing by rememberUpdatedState(isLoading)
    val pullRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var categoryName by remember { mutableStateOf("") }
    var categoryLimit by remember { mutableStateOf("") }

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
                        if (input.all { it.isDigit() }) {
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
                    if (userId != null) {
                        if (categoryName.isNotBlank() && categoryLimit.isNotBlank()) {
                            val categoryLimitValue = categoryLimit.toDoubleOrNull()
                            if (categoryLimitValue != null) {
                                val newCategory = Category(
                                    categoryName = categoryName,
                                    categoryLimit = categoryLimitValue,
                                    userId = userId
                                )

                                firestore.collection("categories")
                                    .add(newCategory)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Category added successfully", Toast.LENGTH_SHORT).show()
                                        categoryName = ""
                                        categoryLimit = ""
                                        showBottomSheet = false
                                        fetchCategories()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Error adding category: $it", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Invalid category limit", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E5EA))
            ) {
                Text(text = "Add Category", fontSize = 18.sp, color = Color.Black)
            }
        }
    }

    if (showBottomSheet) {
        coroutineScope.launch {
            sheetState.show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ClickableText(
                            text = AnnotatedString("Edit"),
                            onClick = { /* Handle edit click */ },
                            style = TextStyle(color = Color(0xFF007AFF), fontSize = 18.sp),
                            modifier = Modifier.padding(start = 5.dp)
                        )

                        AnimatedVisibility(
                            visible = shouldShowTopBarTitle,
                            enter = fadeIn(tween(300)),
                            exit = fadeOut(tween(300))
                        ) {
                            Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                        }

                        Box(modifier = Modifier.size(50.dp))
                    }
                },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SwipeRefresh(
                    state = pullRefreshState,
                    onRefresh = {
                        isLoading = true
                        fetchCategories()
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(start = 20.dp, bottom = innerPadding.calculateBottomPadding() + 120.dp)
                    ) {
                        Text(
                            text = "Categories",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            if (categories.isEmpty()) {
                                Text("No categories found.", fontSize = 16.sp, modifier = Modifier.padding(top = 20.dp))
                            } else {
                                categories.forEach { category ->
                                    CategoryCard(category = category)
                                }
                            }
                        }
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { showBottomSheet = false }
                    ) {
                        bottomSheetContent()
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 90.dp)
                        .height(50.dp)
                        .fillMaxWidth(0.80f)
                        .border(1.dp, Color(10, 132, 255), RoundedCornerShape(15)),
                    onClick = {
                        showBottomSheet = true
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

@Composable
fun CategoryCard(category: Category) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, end = 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = category.categoryName,
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Limit: $${String.format("%.2f", category.categoryLimit)}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray
            )
        }
    }
}

