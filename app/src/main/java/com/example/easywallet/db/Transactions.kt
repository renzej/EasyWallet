package com.example.easywallet.db

data class Transactions(
    val userId: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val date: String = ""
)
