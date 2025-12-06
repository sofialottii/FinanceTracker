package com.example.financetracker.data.repository

import com.example.financetracker.data.local.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface TransactionRepository {
    // Conti
    fun getAccounts(): Flow<List<Account>>
    suspend fun insertAccount(account: Account)

    // Transazioni
    fun getRecentTransactions(): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)

    // Categorie
    fun getCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category)
}

class TransactionRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override fun getAccounts(): Flow<List<Account>> = accountDao.getAllAccounts()

    override suspend fun insertAccount(account: Account) = accountDao.insertAccount(account)

    override fun getRecentTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    override suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)

    override fun getCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    override suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
}