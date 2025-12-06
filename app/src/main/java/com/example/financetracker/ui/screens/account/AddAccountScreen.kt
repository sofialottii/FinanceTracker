package com.example.financetracker.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financetracker.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    onBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }

    val colors = listOf(0xFF6200EE, 0xFF03DAC5, 0xFF4CAF50, 0xFFFF9800, 0xFFF44336, 0xFF607D8B)
    var selectedColor by remember { mutableLongStateOf(colors.first()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo Conto / Carta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Nome Conto
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome Conto (es. Contanti, Hype)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Saldo Iniziale
            OutlinedTextField(
                value = balance,
                onValueChange = { balance = it },
                label = { Text("Saldo Iniziale (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Scegli un Colore:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Selettore Colore
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(colors) { colorLong ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(colorLong))
                            .border(
                                width = if (selectedColor == colorLong) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = colorLong }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val finalBalance = balance.toDoubleOrNull() ?: 0.0
                    viewModel.saveAccount(name, finalBalance, selectedColor.toInt())
                    onBack() // Torna alla Home dopo il salvataggio
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = name.isNotEmpty()
            ) {
                Text("Crea Conto")
            }
        }
    }
}