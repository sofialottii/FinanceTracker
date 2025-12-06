package com.example.financetracker.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val accounts by viewModel.accounts.collectAsState()
    val transactions by viewModel.recentTransactions.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addMockData() }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("I Miei Conti:", style = MaterialTheme.typography.titleLarge)

            // Lista Conti
            LazyColumn(modifier = Modifier.height(150.dp)) {
                items(accounts) { account ->
                    Card(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = account.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "${account.balance} €")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Transazioni Recenti:", style = MaterialTheme.typography.titleLarge)

            // Lista Transazioni
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(transactions) { transaction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(transaction.name, style = MaterialTheme.typography.bodyLarge)
                            Text(transaction.date.toString(), style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            text = "${transaction.amount} €",
                            color = if (transaction.amount < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}