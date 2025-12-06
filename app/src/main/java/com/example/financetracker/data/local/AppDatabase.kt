package com.example.financetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Account::class, Category::class, Transaction::class],
    version = 1,
    exportSchema = false // Per ora false, in prod servirebbe true per le migrazioni
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Qui esponiamo i DAO
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}