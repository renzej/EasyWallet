package com.example.easywallet.navigation

import androidx.annotation.NavigationRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.easywallet.R
import com.example.easywallet.destinations.Destination

@Composable
fun BottomNav(navController : NavController) {
    NavigationBar(
        modifier = Modifier.height(85.dp)
    ) {
        var navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val ic_home = painterResource(id = R.drawable.ic_home)
        val ic_expense = painterResource(id = R.drawable.ic_expense)
        val ic_add = painterResource(id = R.drawable.ic_new)
        val ic_account = painterResource(id = R.drawable.ic_account)
        val ic_news = painterResource(id = R.drawable.ic_news)

        val iconSize = 33.dp

        NavigationBarItem(
            selected = currentDestination?.route == "home",
            onClick = { navController.navigate(Destination.Home.route) {
                popUpTo(Destination.Home.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = ic_home, contentDescription = "Home", modifier = Modifier.size(iconSize)) }
        ) // Home
        NavigationBarItem(
            selected = currentDestination?.route == "expenses",
            onClick = { navController.navigate(Destination.Expenses.route) {
                popUpTo(Destination.Expenses.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = ic_expense, contentDescription = "Expenses", modifier = Modifier.size(iconSize)) }
        ) // Expenses
        NavigationBarItem(
            selected = currentDestination?.route == "transaction",
            onClick = { navController.navigate(Destination.Transactions.route) {
                popUpTo(Destination.Transactions.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = ic_add, contentDescription = "New", modifier = Modifier.size(iconSize)) }
        ) // New Transaction
        NavigationBarItem(
            selected = currentDestination?.route == "account",
            onClick = { navController.navigate(Destination.Account.route) {
                popUpTo(Destination.Account.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = ic_account, contentDescription = "Account", modifier = Modifier.size(iconSize)) }
        ) // Account
        NavigationBarItem(
            selected = currentDestination?.route == "news",
            onClick = { navController.navigate(Destination.News.route) {
                popUpTo(Destination.News.route)
                launchSingleTop = true
            } },
            icon = { Icon(painter = ic_news, contentDescription = "News", modifier = Modifier.size(iconSize)) }
        ) // News
    }
}
