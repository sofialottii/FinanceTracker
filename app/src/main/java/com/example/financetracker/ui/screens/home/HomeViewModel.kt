package com.example.financetracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.local.Account
import com.example.financetracker.data.local.Transaction
import com.example.financetracker.data.local.TransactionType
import com.example.financetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    val accounts: StateFlow<List<Account>> = repository.getAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentTransactions: StateFlow<List<Transaction>> = repository.getRecentTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveTransaction(amount: Double, title: String, isExpense: Boolean, categoryId: Int?) {
        viewModelScope.launch {

            val currentAccounts = repository.getAccounts().first()

            val defaultAccountId = accounts.value.firstOrNull()?.id ?: return@launch

            //importo deve essere negativo per le spese
            val finalAmount = if (isExpense) -kotlin.math.abs(amount) else kotlin.math.abs(amount)

            val newTransaction = Transaction(
                accountId = defaultAccountId,
                categoryId = categoryId,
                name = title,
                amount = finalAmount,
                date = System.currentTimeMillis(),
                type = if (isExpense) TransactionType.EXPENSE else TransactionType.INCOME
            )
            repository.insertTransaction(newTransaction)


            val account = currentAccounts.find { it.id == defaultAccountId }
            if (account != null) {
                val newBalance = account.balance + finalAmount
                repository.updateAccount(account.copy(balance = newBalance))
            }
        }
    }

    fun saveAccount(name: String, balance: Double, color: Int) {
        viewModelScope.launch {
            val newAccount = Account(
                name = name,
                balance = balance,
                color = color
            )
            repository.insertAccount(newAccount)
        }
    }


}