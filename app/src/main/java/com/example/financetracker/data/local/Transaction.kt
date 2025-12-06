package com.example.financetracker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE //se elimino la carta, elimino le transazioni? O RESTRICT?
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL //se elimino la categoria, la transazione rimane ma senza categoria
        )
    ],
    indices = [Index(value = ["accountId"]), Index(value = ["categoryId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accountId: Int,           //collegamento alla carta usata
    val categoryId: Int?,         //collegamento alla categoria (Nullable se la cancelli)
    val name: String,             //titolo/Descrizione
    val amount: Double,           //importo (negativo uscita, positivo entrata)
    val date: Long,               //Timestamp (System.currentTimeMillis()) - facile da ordinare
    val type: TransactionType     //enum per sicurezza
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}