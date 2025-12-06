package com.example.financetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,         //postepay, contanti eccetera
    val balance: Double,      //saldo attuale
    val color: Int            //colore della carta
)