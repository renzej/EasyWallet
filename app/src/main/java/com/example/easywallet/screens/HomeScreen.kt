package com.example.easywallet.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easywallet.api.QuotesManager

@Composable
fun HomeScreen(quotesManager: QuotesManager) {
    val quote = quotesManager.quoteResponse.collectAsState().value

    LaunchedEffect(Unit) {
        quotesManager.getRandomQuote()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        quote?.let {
            Text(text = it.q ?: "No quote available") // Display quote text
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "— ${it.a}") // Display author
        } ?: run {
            Text(text = "Loading quote...") // Display loading message
        }
    }
}
