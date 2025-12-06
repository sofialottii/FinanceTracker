package com.example.financetracker.di

import android.content.Context
import androidx.room.Room
import com.example.financetracker.data.local.AccountDao
import com.example.financetracker.data.local.AppDatabase
import com.example.financetracker.data.local.CategoryDao
import com.example.financetracker.data.local.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Questo modulo vive quanto l'intera app
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finance_tracker_db"
        )
            .fallbackToDestructiveMigration() // Utile in dev: se cambi il DB, lui lo resetta invece di crashare
            .build()
    }

    // Qui "estraiamo" i DAO dal DB per renderli iniettabili direttamente

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideAccountDao(database: AppDatabase): AccountDao {
        return database.accountDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }
}