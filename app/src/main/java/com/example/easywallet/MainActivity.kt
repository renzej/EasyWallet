package com.example.easywallet

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.easywallet.api.NewsManager
import com.example.easywallet.api.QuotesManager
import com.example.easywallet.destinations.Destination
import com.example.easywallet.navigation.BottomNav
import com.example.easywallet.screens.AccountScreen
import com.example.easywallet.screens.CategoriesScreen
import com.example.easywallet.screens.ExpensesScreen
import com.example.easywallet.screens.HomeScreen
import com.example.easywallet.screens.LoginScreen
import com.example.easywallet.screens.NewsScreen
import com.example.easywallet.screens.RegisterScreen
import com.example.easywallet.screens.SplashScreen
import com.example.easywallet.screens.TransactionsScreen
import com.example.easywallet.ui.theme.EasyWalletTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyWalletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Nav Controller
                    val navController = rememberNavController()

                    // News Manager and Quotes Manager
                    val newsManager: NewsManager by viewModels()
                    val quotesManager: QuotesManager by viewModels()

                    EasyWallet(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        newsManager = newsManager,
                        quotesManager = quotesManager
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EasyWallet(
    navController: NavController,
    modifier: Modifier,
    newsManager: NewsManager,
    quotesManager: QuotesManager
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (
                currentRoute != Destination.Splash.route &&
                currentRoute != Destination.Login.route &&
                currentRoute != Destination.Register.route) {
                BottomNav(navController = navController)
            }
        }
    ) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = Destination.Splash.route
        ) {
            composable(Destination.Splash.route) { SplashScreen(navController) }
            composable(Destination.Home.route) { HomeScreen(quotesManager) }
            composable(Destination.Expenses.route) { ExpensesScreen(navController) }
            composable(Destination.Transactions.route) { TransactionsScreen(navController) }
            composable(Destination.Account.route) { AccountScreen() }
            composable(Destination.News.route) { NewsScreen(newsManager) }

            composable(Destination.Login.route) { LoginScreen(navController) }
            composable(Destination.Register.route) { RegisterScreen(navController) }
            composable(Destination.Category.route) { CategoriesScreen() }
        }
    }
}
