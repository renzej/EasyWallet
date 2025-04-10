package com.example.easywallet.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordMatch by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title or Logo
        Text("Create an Account",
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // First Name TextField
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor =  Color(0xff3c632e),
                focusedIndicatorColor = Color(0xff3c632e)
            )
        )

        // Last Name TextField
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor =  Color(0xff3c632e),
                focusedIndicatorColor = Color(0xff3c632e)
            )
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor =  Color(0xff3c632e),
                focusedIndicatorColor = Color(0xff3c632e)
            )
        )

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor =  Color(0xff3c632e),
                focusedIndicatorColor = Color(0xff3c632e)
            )
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor =  Color(0xff3c632e),
                focusedIndicatorColor = Color(0xff3c632e)
            )
        )

        // Confirm Password TextField
        TextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                // Check if passwords match
                isPasswordMatch = password == confirmPassword
            },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor =  Color(0xff3c632e),
                focusedIndicatorColor = Color(0xff3c632e)
            ),
            isError = !isPasswordMatch
        )

        // Error message for password mismatch
        if (!isPasswordMatch) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Sign Up Button
        Button(
            onClick = {
                if (isPasswordMatch) {
                    // Handle Firebase Sign Up logic here
                    // Example: FirebaseAuth.createUserWithEmailAndPassword(email, password)
                    // After successful sign-up, navigate to login or main screen.
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xff8dc44b)),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Sign Up", color = Color.White, fontSize = 18.sp)
        }

        // Back to Login Link
        Text(
            text = "Already have an account? Log in",
            color = Color(0xff8dc44b),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate("login") },
            fontSize = 15.sp
        )
    }
}
