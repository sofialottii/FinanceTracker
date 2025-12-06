package com.example.financetracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Charts : Screen("charts", "Grafici", Icons.Default.Star)
    object AddAccount : Screen("add_account", "Aggiungi Conto", Icons.Default.Add)
}