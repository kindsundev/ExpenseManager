package com.kindsundev.expense.manager.data.model

data class TransactionModel (
    val id: Int,
    val type: String,
    val category: String,
    val amount: Double,
    val date: String,
    val note: String? = null
)