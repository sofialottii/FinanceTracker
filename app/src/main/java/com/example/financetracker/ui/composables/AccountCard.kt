package com.example.financetracker.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.local.Account
import com.example.financetracker.util.toCurrency

@Composable
fun AccountCard(account: Account,
                isSelected: Boolean,
                onClick: () -> Unit,
                onLongClick: () -> Unit) {
    //gradiente basato sul colore salvato (o default viola/blu)
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(account.color), //colore primario
            Color(account.color).copy(alpha = 0.6f) //sfumatura più chiara
        )
    )

    val borderStroke = if (isSelected) BorderStroke(3.dp, MaterialTheme.colorScheme.onSurface) else null

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(280.dp) //larghezza fissa per il carosello
            .height(170.dp)
            .padding(end = 16.dp)
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 12.dp else 4.dp),
        border = borderStroke
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(20.dp)
        ) {
            //nome Carta in alto a sinistra
            Text(
                text = account.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.TopStart)
            )

            //cambia se è selezionata o no
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            } else {
                Text(
                    text = "VISA",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            //saldo in basso a sinistra
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = "Saldo attuale",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = account.balance.toCurrency(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}