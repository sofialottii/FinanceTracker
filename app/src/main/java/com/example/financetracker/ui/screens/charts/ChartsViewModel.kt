package com.example.financetracker.ui.screens.charts

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.local.TransactionType
import com.example.financetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CategoryStat(
    val categoryName: String,
    val totalAmount: Double,
    val color: Int,
    val percentage: Float
)

@HiltViewModel
class ChartsViewModel @Inject constructor(
    repository: TransactionRepository
) : ViewModel() {

    val expensesByCategory = repository.getRecentTransactions()
        .map { transactions ->
            val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
            val totalExpense = expenses.sumOf { kotlin.math.abs(it.amount) }


            expenses.groupBy { it.categoryId }
                .map { (catId, list) ->
                    val sum = list.sumOf { kotlin.math.abs(it.amount) }

                    CategoryStat(
                        categoryName = list.first().name,
                        totalAmount = sum,
                        color = android.graphics.Color.HSVToColor(floatArrayOf((catId ?: 0) * 30f % 360, 0.8f, 0.9f)),
                        percentage = if (totalExpense > 0) (sum / totalExpense).toFloat() else 0f
                    )
                }.sortedByDescending { it.percentage }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}