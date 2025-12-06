package com.example.financetracker.ui.screens.charts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financetracker.ui.composables.PieChart
import com.example.financetracker.util.toCurrency

@Composable
fun ChartsScreen(
    viewModel: ChartsViewModel = hiltViewModel()
) {
    val categoryStats by viewModel.expensesByCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Spese per Categoria", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(32.dp))

        if (categoryStats.isNotEmpty()) {
            // IL GRAFICO
            PieChart(data = categoryStats)

            Spacer(modifier = Modifier.height(32.dp))

            // LA LEGENDA
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(categoryStats) { stat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            androidx.compose.foundation.Canvas(modifier = Modifier.size(16.dp)) {
                                drawCircle(Color(stat.color))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stat.categoryName)
                        }
                        Text(
                            text = "${stat.totalAmount.toCurrency()} (${(stat.percentage * 100).toInt()}%)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nessuna spesa registrata ancora.")
            }
        }
    }
}