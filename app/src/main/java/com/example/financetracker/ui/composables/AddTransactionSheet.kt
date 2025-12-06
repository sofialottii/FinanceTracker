package com.example.financetracker.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.local.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(
    availableCategories: List<Category>,
    onSave: (Double, String, Boolean, Int?) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) } // Default è una spesa
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text("Nuova Transazione", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle Entrata/Uscita
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Entrata")
            Switch(
                checked = isExpense,
                onCheckedChange = { isExpense = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text("Uscita", fontWeight = if(isExpense) androidx.compose.ui.text.font.FontWeight.Bold else null)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Importo
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Importo (€)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Descrizione
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrizione (es. Spesa, Pizza...)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Categoria:", style = MaterialTheme.typography.bodyMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(availableCategories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = if (selectedCategory == category) null else category },
                    label = { Text(category.name) },
                    leadingIcon = if (selectedCategory == category) {
                        { Icon(androidx.compose.material.icons.Icons.Default.Check, null) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottone Salva
        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull()
                if (amount != null && description.isNotEmpty()) {
                    onSave(amount, description, isExpense, selectedCategory?.id)
                    onDismiss()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = amountText.isNotEmpty() // Disabilitato se vuoto
        ) {
            Text("Salva Transazione")
        }
    }
}