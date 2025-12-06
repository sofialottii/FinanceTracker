package com.example.financetracker.data.local

import androidx.room.TypeConverter

class Converters {

    //converte l'enum in stringa per salvarlo nel database
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    //fa il contrario per l'app
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}