package com.example.easywallet.destinations

sealed class Destination(val route: String) {
    object Splash : Destination("splash")
    object Home : Destination("home")
    object Expenses : Destination("expenses")
    object Transactions : Destination("transaction")
    object Account : Destination("account")
    object News : Destination("news")
    object Category : Destination("categories")
}