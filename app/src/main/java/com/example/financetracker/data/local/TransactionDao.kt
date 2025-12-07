package com.example.financetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    //prende tutte le transazioni ordinate per data (dalla più recente)
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    //filtra per range di date (utile per "Spese di questo mese")
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByAccountAndDate(accountId: Int, startDate: Long, endDate: Long): Flow<List<Transaction>>

    // Filtra per Data (Tutti i conti) - Questa ce l'avevi già, ma controlla
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getAllTransactionsByDate(startDate: Long, endDate: Long): Flow<List<Transaction>>

    // smma totale uscite
    //per la somma precisa SQL: "SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE'"

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}