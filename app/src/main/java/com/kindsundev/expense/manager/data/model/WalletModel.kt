package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Parcelize
data class WalletModel(
    val id: Int? = 0,
    val name: String? = "",
    val currency: String? = "",
    val origin: Double? = 0.0,
    val balance: Double? = 0.0,
    val transactions: HashMap<String, HashMap<String, TransactionModel>>? = null,
    val plans: HashMap<String, HashMap<String, PlanModel>>? = null
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

    fun getBills(): ArrayList<BillModel> {
        val bills = ArrayList<BillModel>()
        transactions?.let {
            for ((date, map) in it) {
                val transactions = ArrayList<TransactionModel>()
                for ((_, model) in map) { transactions.add(model) }
                val bill = BillModel(date, transactions)
                bills.add(bill)
            }
        }
        return bills
    }

    fun getPlanMap(): HashMap<String, PlanModel> {
        val data = HashMap<String, PlanModel>()
        plans?.let {
            for ((date, map) in it) {
                for ((_, model) in map) {
                    data[date] = model
                }
            }
        }
        return data
    }
}