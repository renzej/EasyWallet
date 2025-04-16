package com.example.easywallet

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easywallet.destinations.Destination
import com.example.easywallet.screens.LoginScreen
import com.example.easywallet.screens.RegisterScreen
import com.example.easywallet.ui.theme.EasyWalletTheme

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyWalletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Nav Controller
                    val navController = rememberNavController()
                    val context = this

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController, context) }
                        composable("register") { RegisterScreen(navController) }
                    }
                }
            }
        }
    }
}
