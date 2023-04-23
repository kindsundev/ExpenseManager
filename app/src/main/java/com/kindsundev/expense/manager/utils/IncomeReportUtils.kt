package com.kindsundev.expense.manager.utils

import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel

class IncomeReportUtils {
    private val salary = ArrayList<TransactionModel>()
    private val bons = ArrayList<TransactionModel>()
    private val interestRate = ArrayList<TransactionModel>()
    private val other = ArrayList<TransactionModel>()

    fun getPercentage(bills : ArrayList<BillModel>): ArrayList<PieEntry> {
        clearCurrentData()
        val transactions = getTypeOfIncome(bills)
        groupClassification(transactions)
        return initPieData(transactions)
    }

    private fun clearCurrentData() {
        salary.clear()
        bons.clear()
        interestRate.clear()
        other.clear()
    }

    private fun getTypeOfIncome(bills : ArrayList<BillModel>): ArrayList<TransactionModel> {
        val transactions = ArrayList<TransactionModel>()
        bills.forEach { billModel ->
            billModel.transactions?.forEach { transaction ->
                if (transaction.type == Constant.TRANSACTION_TYPE_INCOME) {
                    transactions.add(transaction)
                }
            }
        }
        return transactions
    }

    private fun groupClassification(transactions : ArrayList<TransactionModel>) {
        transactions.forEach {
            when(it.category) {
                "Salary" -> salary.add(it)
                "Bons" -> bons.add(it)
                "Interest Rate" -> interestRate.add(it)
                "Other" -> other.add(it)
                else -> {}
            }
        }
    }

    private fun initPieData(transactions: ArrayList<TransactionModel>): ArrayList<PieEntry> {
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(calculatePercentage(salary, transactions), "Salary"))
        data.add(PieEntry(calculatePercentage(bons, transactions), "Bons"))
        data.add(PieEntry(calculatePercentage(interestRate, transactions), "Interest Rate"))
        data.add(PieEntry(calculatePercentage(other, transactions), "Other"))
        cleanPercentageJunkData(data)
        return data
    }

    private fun calculatePercentage(
        list : ArrayList<TransactionModel>, transactions : ArrayList<TransactionModel>
    ): Float {
        return (list.size.toFloat() / transactions.size.toFloat()) * 100
    }

    private fun cleanPercentageJunkData(data: ArrayList<PieEntry>) {
        var count = 0
        for (entry in data) {
            if (entry.x == 0.0f && entry.y.isNaN()) { count++ }
        }
        if (count == data.size) data.clear()
    }

}