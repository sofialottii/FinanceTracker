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