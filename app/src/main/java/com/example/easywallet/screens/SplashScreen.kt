package com.example.easywallet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.easywallet.R
import kotlinx.coroutines.delay
import com.example.easywallet.destinations.Destination

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(Destination.Home.route) {
            popUpTo(Destination.Splash.route) { inclusive = true }
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