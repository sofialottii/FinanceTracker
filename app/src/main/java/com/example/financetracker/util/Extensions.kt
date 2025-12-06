package com.example.financetracker.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale.ITALY).format(this)
}

fun Long.toReadableDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}