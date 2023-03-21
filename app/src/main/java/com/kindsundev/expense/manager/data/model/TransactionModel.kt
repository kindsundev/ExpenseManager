package com.kindsundev.expense.manager.data.model

data class TransactionModel (
    val id: Int? = 0,
    val type: String? = "",
    val category: String? = "",
    val amount: Double? = 0.0,
    val date: String? = "",
    val note: String? = ""
)