package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletModel(
    val id: Int? = 0,
    val name: String? = "",
    val currency: String? = "",
    val origin: Double? = 0.0,
    val balance: Double? = 0.0,
    val transactions: HashMap<String, HashMap<String, TransactionModel>>? = null,
) : Parcelable