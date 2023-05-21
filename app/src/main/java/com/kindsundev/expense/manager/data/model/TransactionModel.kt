package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionModel(
    val id: Int? = 0,
    val type: String? = "",
    val category: String? = "",
    val amount: Double? = 0.0,
    val date: String? = "",
    val note: String? = "",
    val planId: Int? = null
) : Parcelable