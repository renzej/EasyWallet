package com.example.easywallet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.easywallet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.Center // Center vertically
    ) {
        // Logo Image
        Image(
            painter = painterResource(id = R.drawable.easywalletlogotext), // Replace with your logo's resource ID
            contentDescription = "App Logo",
            modifier = Modifier
                .padding(bottom = 32.dp) // Space between the logo and text fields
        )

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            trailingIcon = {
                if (username.isNotEmpty()) {
                    IconButton(onClick = { username = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Icon"
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            trailingIcon = {
                if (password.isNotEmpty()) {
                    IconButton(onClick = { password = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Icon"
                        )
                    }
                }
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Login Button
        Button(
            onClick = { /* Handle login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xff8dc44b)),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                "Login",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        // Sign Up Link
        Text(
            text = "Don't have an account? Sign Up",
            color = Color(0xff8dc44b), // Link color (you can change it to your desired color)
            modifier = Modifier
                .padding(top = 16.dp) // Space from the Login button
                .clickable {
                    navController.navigate("register")
                },
            fontSize = 15.sp
        )
    }
}
