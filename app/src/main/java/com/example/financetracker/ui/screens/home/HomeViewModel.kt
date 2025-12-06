package com.example.financetracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.local.Account
import com.example.financetracker.data.local.Category
import com.example.financetracker.data.local.Transaction
import com.example.financetracker.data.local.TransactionType
import com.example.financetracker.data.repository.TransactionRepository
import com.example.financetracker.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimeRange(val label: String) {
    ALL("Tutto"),
    LAST_30_DAYS("30 Giorni"),
    THIS_MONTH("Mese Corr.")
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _timeRange = MutableStateFlow(TimeRange.THIS_MONTH)
    val currentTimeRange = _timeRange

    val accounts: StateFlow<List<Account>> = repository.getAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val recentTransactions = _timeRange.flatMapLatest { range ->
        when (range) {
            TimeRange.ALL -> repository.getRecentTransactions()
            TimeRange.THIS_MONTH -> repository.getTransactionsByDate(DateUtils.getStartOfMonth(), DateUtils.getEndOfDay())
            TimeRange.LAST_30_DAYS -> repository.getTransactionsByDate(DateUtils.getStartOfLast30Days(), DateUtils.getEndOfDay())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    fun saveCategory(name: String, color: Int) {
        viewModelScope.launch {
            val newCategory = Category(
                name = name,
                color = color
            )
            repository.insertCategory(newCategory)
        }
    }

    fun setTimeRange(range: TimeRange) {
        _timeRange.value = range
    }


}