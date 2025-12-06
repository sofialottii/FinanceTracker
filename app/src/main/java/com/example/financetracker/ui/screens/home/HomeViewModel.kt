package com.example.financetracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.local.Account
import com.example.financetracker.data.local.Category
import com.example.financetracker.data.local.Transaction
import com.example.financetracker.data.local.TransactionType
import com.example.financetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.graphics.Color as AndroidColor

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
            //importo deve essere negativo per le spese
            val finalAmount = if (isExpense) -kotlin.math.abs(amount) else kotlin.math.abs(amount)

            val defaultAccountId = accounts.value.firstOrNull()?.id ?: return@launch

            val newTransaction = Transaction(
                accountId = defaultAccountId,
                categoryId = categoryId,
                name = title,
                amount = finalAmount,
                date = System.currentTimeMillis(),
                type = if (isExpense) TransactionType.EXPENSE else TransactionType.INCOME
            )
            repository.insertTransaction(newTransaction)


            val account = accounts.value.find { it.id == defaultAccountId }
            if (account != null) {
                val newBalance = account.balance + finalAmount
                repository.insertAccount(account.copy(balance = newBalance))
            }
        }
    }

    // 3. dati di prova (cancellerò)
    fun addMockData() {
        viewModelScope.launch {

            val card = Account(name = "Postepay", balance = 1500.0, color = AndroidColor.BLUE)
            repository.insertAccount(card)


            val catSpesa = Category(name = "Spesa", color = AndroidColor.RED, iconName = "cart")
            val catStipendio = Category(name = "Stipendio", color = AndroidColor.GREEN, iconName = "cash")
            repository.insertCategory(catSpesa)
            repository.insertCategory(catStipendio)

            repository.insertTransaction(
                Transaction(
                    accountId = 1,
                    categoryId = 2,
                    name = "Stipendio Gennaio",
                    amount = 2000.0,
                    date = System.currentTimeMillis(),
                    type = TransactionType.INCOME
                )
            )
            repository.insertTransaction(
                Transaction(
                    accountId = 1,
                    categoryId = 1,
                    name = "Esselunga",
                    amount = -85.50,
                    date = System.currentTimeMillis(),
                    type = TransactionType.EXPENSE
                )
            )
        }
    }
}