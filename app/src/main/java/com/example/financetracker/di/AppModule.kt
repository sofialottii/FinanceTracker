package com.example.financetracker.di

import android.content.Context
import androidx.room.Room
import com.example.financetracker.data.local.AccountDao
import com.example.financetracker.data.local.AppDatabase
import com.example.financetracker.data.local.CategoryDao
import com.example.financetracker.data.local.TransactionDao
import com.example.financetracker.data.repository.TransactionRepository
import com.example.financetracker.data.repository.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finance_tracker_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

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

    // --- AGGIUNGI QUESTO BLOCCO QUI IN FONDO ---
    @Provides
    @Singleton
    fun provideRepository(
        accountDao: AccountDao,
        transactionDao: TransactionDao,
        categoryDao: CategoryDao
    ): TransactionRepository {
        // Hilt vede che hai bisogno dei DAO, li cerca qui sopra, li trova e li inietta qui. Magia.
        return TransactionRepositoryImpl(accountDao, transactionDao, categoryDao)
    }
}