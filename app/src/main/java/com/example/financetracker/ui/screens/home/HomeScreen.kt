package com.example.financetracker.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financetracker.ui.composables.AccountCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val accounts by viewModel.accounts.collectAsState()
    val transactions by viewModel.recentTransactions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "I Miei Conti",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp), // Padding iniziale e finale
            modifier = Modifier.fillMaxWidth()
        ) {
            items(accounts) { account ->
                AccountCard(account = account)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //sezione Transazioni
        Text(
            text = "Ultimi movimenti",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // lista verticale transazioni
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(transactions) { transaction ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(transaction.name, fontWeight = FontWeight.Medium)
                        Text(
                            "Oggi", // TODO: Formattare la data
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = "${if (transaction.amount > 0) "+" else ""}${transaction.amount} €",
                        color = if (transaction.amount < 0) Color.Red else Color(0xFF008000), // Rosso o Verde scuro
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}