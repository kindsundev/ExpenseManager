package com.kindsundev.expense.manager.data.model

import java.io.Serializable

/* Why i don't implement Parcelable, because HashMap<*,*> property implements Serializable */

data class BillModel(
    val date: String? = "",
    var mapTransactions: HashMap<String, TransactionModel>? = HashMap(1)
) : Serializable