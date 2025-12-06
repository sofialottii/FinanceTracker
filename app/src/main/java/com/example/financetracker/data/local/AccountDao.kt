package com.example.financetracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    //restituisce la lista dei conti come flusso continuo di dati
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)
}