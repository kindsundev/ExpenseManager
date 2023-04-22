package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import com.kindsundev.expense.manager.common.Logger
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
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

    fun calculateBalanceEveryDayForLastSevenDays(): ArrayList<Double> {
        val currentBalance = balance
        val balanceInLastSevenDays = ArrayList<Double>()
        val amountInLastSevenDays = calculateAmountEveryDayForLastSevenDays()
        currentBalance?.let {
            var result = currentBalance
            amountInLastSevenDays.forEach { amount ->
                if (amount > 0) result -= amount else result += (-amount)
                balanceInLastSevenDays.add(result)
            }
            if (balanceInLastSevenDays.isNotEmpty()) {
                balanceInLastSevenDays.removeLast()         // change currentBalance
                balanceInLastSevenDays.reverse()            // before sort to get last seven days
                balanceInLastSevenDays.add(currentBalance)
            }
        }
//        balanceInLastSevenDays.forEach { Logger.info(String.format("%.0f", it)) }
        return balanceInLastSevenDays
    }

    private fun calculateAmountEveryDayForLastSevenDays(): ArrayList<Double> {
        val amountInLastSevenDays = ArrayList<Double>()
        val listOfLastSevenDays = getListOfLastSevenDays()
        if (listOfLastSevenDays.isNotEmpty()) {
            listOfLastSevenDays.forEach { bill ->
                val result = bill.calculateBalance()
                amountInLastSevenDays.add(result)
//                Logger.error(result.toString())
            }
        }
        return amountInLastSevenDays
    }

    private fun getListOfLastSevenDays(): List<BillModel> {
        var lastSevenDays : List<BillModel> = listOf()
        val bills = getBills()
        if (bills.isNotEmpty()) {
            lastSevenDays = bills.sortedByDescending { bill ->
                bill.date?.let {
                    SimpleDateFormat("dd E MMMM yyyy", Locale.ENGLISH).parse(it)
                }
            }.take(7)
        }
        return lastSevenDays
    }

    private fun getBills(): ArrayList<BillModel> {
        val bills = ArrayList<BillModel>()
        val data = transactions
        data?.let {
            for ((date, map) in data) {
                val transactions = ArrayList<TransactionModel>()
                for ((_, model) in map) { transactions.add(model) }
                val bill = BillModel(date, transactions)
                bills.add(bill)
            }
        }
        return bills
    }
}