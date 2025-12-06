package com.example.financetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,         //spesa, svago eccetera
    val color: Int,
    val iconName: String? = null //opzionale se vogliamo mettere un'icona
)