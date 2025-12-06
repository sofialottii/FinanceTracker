package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.ui.Screen
import com.example.financetracker.ui.composables.AddTransactionSheet
import com.example.financetracker.ui.screens.charts.ChartsScreen
import com.example.financetracker.ui.screens.home.HomeScreen
import com.example.financetracker.ui.screens.home.HomeViewModel
import com.example.financetracker.ui.theme.FinanceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceTrackerTheme {
                val navController = rememberNavController()
                // Per sapere quale tab è selezionato
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val viewModel: HomeViewModel = hiltViewModel()

                //stato per la tendina
                var showSheet by remember { mutableStateOf(false) }
                val sheetState = rememberModalBottomSheetState()
                val categories by viewModel.categories.collectAsState()

                Scaffold(
                    // --- BOTTOM BAR ---
                    bottomBar = {
                        NavigationBar {
                            val items = listOf(Screen.Home, Screen.Charts)
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    label = { Text(screen.title) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            // Pulisce lo stack per evitare accumulo di schermate
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    },
                    // --- FAB (Il pulsante +) ---
                    floatingActionButton = {
                        if (currentRoute == Screen.Home.route) {
                            FloatingActionButton(
                                onClick = {
                                    showSheet = true
                                }
                            ) {
                                Text("+", style = MaterialTheme.typography.headlineMedium)
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(viewModel = viewModel)
                        }
                        composable(Screen.Charts.route) {
                            ChartsScreen()
                        }
                    }

                    if (showSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showSheet = false },
                            sheetState = sheetState
                        ) {
                            AddTransactionSheet(
                                availableCategories = categories,
                                onSave = { amount, desc, isExp, catId ->
                                    viewModel.saveTransaction(amount, desc, isExp, catId)
                                },
                                onDismiss = { showSheet = false }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanceTrackerTheme {
        Greeting("Android")
    }
}