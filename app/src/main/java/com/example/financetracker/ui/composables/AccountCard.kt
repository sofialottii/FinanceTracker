package com.example.financetracker.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.data.local.Account
import com.example.financetracker.util.toCurrency

@Composable
fun AccountCard(account: Account) {
    //gradiente basato sul colore salvato (o default viola/blu)
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(account.color), //colore primario
            Color(account.color).copy(alpha = 0.6f) //sfumatura più chiara
        )
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(280.dp) //larghezza fissa per il carosello
            .height(170.dp)
            .padding(end = 16.dp), //spazio tra una carta e l'altra
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient) //applichiamo il gradiente
                .padding(20.dp)
        ) {
            //nome Carta in alto a sinistra
            Text(
                text = account.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.TopStart)
            )

            //lgo in alto a destra
            Text(
                text = "VISA", // O Mastercard, o niente
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopEnd)
            )

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