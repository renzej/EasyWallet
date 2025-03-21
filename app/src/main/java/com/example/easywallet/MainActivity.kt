package com.example.easywallet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.easywallet.api.NewsManager
import com.example.easywallet.destinations.Destination
import com.example.easywallet.navigation.BottomNav
import com.example.easywallet.screens.AccountScreen
import com.example.easywallet.screens.CategoriesScreen
import com.example.easywallet.screens.ExpensesScreen
import com.example.easywallet.screens.HomeScreen
import com.example.easywallet.screens.NewsScreen
import com.example.easywallet.screens.SplashScreen
import com.example.easywallet.screens.TransactionsScreen
import com.example.easywallet.ui.theme.EasyWalletTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyWalletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Nav controller
                    val navController = rememberNavController()

                    val newsManager: NewsManager by viewModels()


                    EasyWallet(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        newsManager = newsManager
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyWallet(navController: NavController, modifier: Modifier, newsManager: NewsManager) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute !in listOf("login", "splash", "home", "expenses")) {
                when (currentRoute) {
                    "transaction" -> {
                        CenterAlignedTopAppBar(
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
                                        style = androidx.compose.ui.text.TextStyle(
                                            color = Color(0xFF007AFF),
                                            fontSize = 18.sp
                                        ),
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                    Text(
                                        text = "Transaction",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    IconButton(onClick = { /* Handle add transaction */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Add Transaction",
                                            tint = Color(0xFF007AFF),
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.height(80.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (currentRoute != Destination.Splash.route)
                BottomNav(navController = navController)
        }
    ) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = Destination.Splash.route
        ) {
            composable(Destination.Splash.route) { SplashScreen(navController) }
            composable(Destination.Home.route) { HomeScreen() }
            composable(Destination.Expenses.route) { ExpensesScreen(navController) }
            composable(Destination.Transactions.route) { TransactionsScreen() }
            composable(Destination.Account.route) { AccountScreen() }
            composable(Destination.News.route) { NewsScreen(newsManager) }

            composable("categories") { CategoriesScreen() }
        }
    }
}

