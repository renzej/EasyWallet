package com.example.easywallet.screens

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.easywallet.MainActivity
import com.example.easywallet.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    context: Context
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            trailingIcon = {
                if (email.isNotEmpty()) {
                    IconButton(onClick = { email = "" }) {
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
            onClick = {
                performLogIn(email, password, context)
            },
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

        // Anonymous Login Link
        Text(
            text = "Continue as Guest",
            color = Color(0xff8dc44b),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    performAnonymousLogin(context)
                }
        )

        // Sign Up Link
        Text(
            text = "Don't have an account? Sign Up",
            color = Color(0xff8dc44b), // Link color (you can change it to your desired color)
            modifier = Modifier
                .padding(top = 5.dp) // Space from the Login button
                .clickable {
                    navController.navigate("register")
                },
            fontSize = 15.sp
        )
    }
}

private fun performLogIn(
    email: String,
    password: String,
    context: Context
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Log In Successful", Toast.LENGTH_SHORT).show()

                // Start MainActivity
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)

                // Finish SignInActivity
                (context as? android.app.Activity)?.finish()
            } else {
                Toast.makeText(context, "Failed Log In", Toast.LENGTH_LONG).show()
            }
        }
}

private fun performAnonymousLogin(context: Context) {
    val auth = FirebaseAuth.getInstance()

    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Anonymous Sign In Successful", Toast.LENGTH_SHORT).show()

                // Start MainActivity
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)

                // Finish SignInActivity
                (context as? android.app.Activity)?.finish()
            } else {
                Toast.makeText(context, "Anonymous Sign In Failed", Toast.LENGTH_LONG).show()
            }
        }
}

