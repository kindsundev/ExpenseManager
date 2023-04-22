package com.kindsundev.expense.manager.utils

import com.kindsundev.expense.manager.data.model.BillModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BalanceReportUtils {

    fun calculateBalanceEveryDayForLastSevenDays(
        currentBalance: Double,
        bills: ArrayList<BillModel>
    ): ArrayList<Double> {
        val balanceInLastSevenDays = ArrayList<Double>()
        val amountInLastSevenDays = calculateAmountEveryDayForLastSevenDays(bills)
        currentBalance.let {
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

    private fun calculateAmountEveryDayForLastSevenDays(bills: ArrayList<BillModel>): ArrayList<Double> {
        val amountInLastSevenDays = ArrayList<Double>()
        val listOfLastSevenDays = getListOfLastSevenDays(bills)
        if (listOfLastSevenDays.isNotEmpty()) {
            listOfLastSevenDays.forEach { bill ->
                val result = bill.calculateBalanceForDay()
                amountInLastSevenDays.add(result)
//                Logger.error(result.toString())
            }
        }
        return amountInLastSevenDays
    }

    private fun getListOfLastSevenDays(bills: ArrayList<BillModel>): List<BillModel> {
        var lastSevenDays : List<BillModel> = listOf()
        if (bills.isNotEmpty()) {
            lastSevenDays = bills.sortedByDescending { bill ->
                bill.date?.let {
                    SimpleDateFormat("dd E MMMM yyyy", Locale.ENGLISH).parse(it)
                }
            }.take(7)
        }
        return lastSevenDays
    }
}