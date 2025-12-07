package com.example.financetracker.data.repository

import com.example.financetracker.data.local.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface TransactionRepository {
    // Conti
    fun getAccounts(): Flow<List<Account>>
    suspend fun insertAccount(account: Account)
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(account: Account)

    // Transazioni
    fun getRecentTransactions(): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)
    fun getTransactionsByDate(startDate: Long, endDate: Long): Flow<List<Transaction>>
    suspend fun deleteTransaction(transaction: Transaction)
    fun getTransactionsByAccount(accountId: Int?, startDate: Long, endDate: Long): Flow<List<Transaction>>

    // Categorie
    fun getCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun deleteCategory(category: Category)
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

    override fun getTransactionsByDate(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }

    override fun getCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    override suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    override fun getTransactionsByAccount(accountId: Int?, startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return if (accountId == null) {
            // Se ID è null, restituisci TUTTO (vista globale)
            transactionDao.getAllTransactionsByDate(startDate, endDate)
        } else {
            // Altrimenti filtra per quella carta
            transactionDao.getTransactionsByAccountAndDate(accountId, startDate, endDate)
        }
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}