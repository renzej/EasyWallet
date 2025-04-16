package com.example.easywallet.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.easywallet.R
import com.example.easywallet.SignInActivity
import kotlinx.coroutines.delay
import com.example.easywallet.destinations.Destination
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        delay(1000)
        if (currentUser != null) {
            // User is logged in, go to home screen
            navController.navigate(Destination.Home.route) {
                popUpTo(Destination.Splash.route) { inclusive = true }
            }
        } else {
            // User is not logged in, launch SignInActivity
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)

            // Finish MainActivity so it doesn't stay in back stack
            if (context is Activity) {
                context.finish()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
         contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Space from the Top

            Image(
                painter = painterResource(id = R.drawable.easywalletlogoimage),
                contentDescription = "Splash Image",
                modifier = Modifier
                    .size(175.dp)
                    .offset(y = 60.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Space between the two logos

            Column( // Group together the 2nd logo and the text
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.easywalletlogotext),
                    contentDescription = "Logo Text",
                    modifier = Modifier
                        .size(175.dp)
                        .offset(y = 65.dp)
                )
                Text(
                    text = "from Astra Labs",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(78.dp))
        }
    }
}