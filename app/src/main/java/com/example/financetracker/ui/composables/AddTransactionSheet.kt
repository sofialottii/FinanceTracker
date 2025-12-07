package com.example.financetracker.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.financetracker.data.local.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(
    availableCategories: List<Category>,
    onSave: (Double, String, Boolean, Int?) -> Unit,
    onNewCategory: (String, Int) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) } // Default è una spesa
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    var showNewCategoryDialog by remember { mutableStateOf(false) }

    var showDeleteCatDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

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
        Spacer(modifier = Modifier.height(8.dp))


        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(availableCategories) { category ->

                val isSelected = selectedCategory == category
                val categoryColor = Color(category.color)

                Surface(
                    color = if (isSelected) categoryColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) categoryColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .combinedClickable(
                            onClick = {
                                // Selezione / Deselezione
                                selectedCategory = if (isSelected) null else category
                            },
                            onLongClick = {
                                // Eliminazione
                                categoryToDelete = category
                                showDeleteCatDialog = true
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Se selezionato, mostra la spunta
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp).padding(end = 8.dp)
                            )
                        }

                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showNewCategoryDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nuova",
                            modifier = Modifier.size(18.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "Nuova",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
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


    if (showNewCategoryDialog) {
        CreateCategoryDialog(
            onDismiss = { showNewCategoryDialog = false },
            onConfirm = { name, color ->
                onNewCategory(name, color)
                showNewCategoryDialog = false
            }
        )
    }

    if (showDeleteCatDialog && categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteCatDialog = false
                categoryToDelete = null
            },
            title = { Text("Elimina Categoria") },
            text = {
                Text("Vuoi eliminare '${categoryToDelete?.name}'?\nLe transazioni associate NON verranno cancellate, ma perderanno la categoria.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        categoryToDelete?.let { onDeleteCategory(it) }
                        //se avevamo selezionato proprio quella categoria, deselezioniamola
                        if (selectedCategory == categoryToDelete) {
                            selectedCategory = null
                        }
                        showDeleteCatDialog = false
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Elimina")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteCatDialog = false }) {
                    Text("Annulla")
                }
            }
        )
    }
}

@Composable
fun CreateCategoryDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var newCatName by remember { mutableStateOf("") }
    val colors = listOf(0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF2196F3, 0xFF4CAF50, 0xFFFFC107, 0xFFFF5722, 0xFF795548, 0xFF607D8B)
    var selectedColor by remember { mutableLongStateOf(colors.first()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Crea Categoria", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newCatName,
                    onValueChange = { newCatName = it },
                    label = { Text("Nome (es. Palestra)") }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Scegli Colore:")
                Spacer(modifier = Modifier.height(8.dp))

                // Color Picker
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                                .border(if(selectedColor == color) 2.dp else 0.dp, Color.Black, CircleShape)
                                .clickable { selectedColor = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Annulla") }
                    Button(
                        onClick = {
                            if (newCatName.isNotEmpty()) onConfirm(newCatName, selectedColor.toInt())
                        },
                        enabled = newCatName.isNotEmpty()
                    ) { Text("Crea") }
                }
            }
        }
    }
}