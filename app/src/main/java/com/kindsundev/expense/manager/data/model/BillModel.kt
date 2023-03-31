package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillModel(
    val date: String? = "",
    var transactions: ArrayList<TransactionModel>? = ArrayList()
) : Parcelable