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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
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

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount = _selectedAccount.asStateFlow()

    private val _timeRange = MutableStateFlow(TimeRange.THIS_MONTH)
    val currentTimeRange = _timeRange

    val accounts = repository.getAccounts()
        .onEach { list ->
            // Se non ho selezionato nulla e ho dei conti, seleziono il primo di default
            if (_selectedAccount.value == null && list.isNotEmpty()) {
                _selectedAccount.value = list.first()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val recentTransactions = combine(_selectedAccount, _timeRange) { account, timeRange ->
        // Calcoliamo le date in base al filtro
        val (start, end) = when (timeRange) {
            TimeRange.ALL -> 0L to Long.MAX_VALUE
            TimeRange.THIS_MONTH -> DateUtils.getStartOfMonth() to DateUtils.getEndOfDay()
            TimeRange.LAST_30_DAYS -> DateUtils.getStartOfLast30Days() to DateUtils.getEndOfDay()
        }
        // Restituiamo la coppia di parametri per la query successiva
        Triple(account?.id, start, end)
    }.flatMapLatest { (accountId, start, end) ->
        // Eseguiamo la query vera
        repository.getTransactionsByAccount(accountId, start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectAccount(account: Account) {
        _selectedAccount.value = account
    }

    val categories = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveTransaction(amount: Double, title: String, isExpense: Boolean, categoryId: Int?, date: Long) {
        viewModelScope.launch {

            val currentAccounts = repository.getAccounts().first()

            val defaultAccountId = accounts.value.firstOrNull()?.id ?: return@launch

            val targetAccount = _selectedAccount.value ?: return@launch

            //importo deve essere negativo per le spese
            val finalAmount = if (isExpense) -kotlin.math.abs(amount) else kotlin.math.abs(amount)

            val newTransaction = Transaction(
                accountId = targetAccount.id,
                categoryId = categoryId,
                name = title,
                amount = finalAmount,
                date = date,
                type = if (isExpense) TransactionType.EXPENSE else TransactionType.INCOME
            )
            repository.insertTransaction(newTransaction)

            val newBalance = targetAccount.balance + finalAmount
            repository.updateAccount(targetAccount.copy(balance = newBalance))

            _selectedAccount.value = targetAccount.copy(balance = newBalance)

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

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)

            val currentAccounts = repository.getAccounts().first()
            val account = currentAccounts.find { it.id == transaction.accountId }

            if (account != null) {
                val newBalance = account.balance - transaction.amount
                repository.updateAccount(account.copy(balance = newBalance))
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)

            //se stavo guardando proprio quel conto, resetto la selezione
            if (_selectedAccount.value?.id == account.id) {
                _selectedAccount.value = null
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }


}