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
) : Parcelable {

    fun getTransactionCount(): Int {
        var count = 0
        if (transactions != null) {
            for (entry in transactions.values) {
                count += entry.size
            }
        }
        return count
    }
}