package com.kindsundev.expense.manager.utils

import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel

class ExpenseReportUtils {
    private val needFul = ArrayList<TransactionModel>()
    private val enjoy = ArrayList<TransactionModel>()
    private val offering = ArrayList<TransactionModel>()
    private val health = ArrayList<TransactionModel>()
    private val child = ArrayList<TransactionModel>()
    private val other = ArrayList<TransactionModel>()

    fun getPercentage(bills : ArrayList<BillModel>): ArrayList<PieEntry> {
        clearCurrentData()
        val transactions = getTypeOfExpense(bills)
        groupClassification(transactions)
        return initPieData(transactions)
    }

    private fun clearCurrentData() {
        needFul.clear()
        enjoy.clear()
        offering.clear()
        health.clear()
        child.clear()
        other.clear()
    }

    private fun getTypeOfExpense(bills : ArrayList<BillModel>): ArrayList<TransactionModel> {
        val transactions = ArrayList<TransactionModel>()
        bills.forEach { billModel ->
            billModel.transactions?.forEach { transaction ->
                if (transaction.type == Constant.TRANSACTION_TYPE_EXPENSE) {
                    transactions.add(transaction)
                }
            }
        }
        return transactions
    }

    private fun groupClassification(transactions : ArrayList<TransactionModel>) {
        transactions.forEach {
            when(it.category) {
                "Eat" -> needFul.add(it)
                "Bill" -> needFul.add(it)
                "Move" -> needFul.add(it)
                "House" -> needFul.add(it)
                "Shopping" -> needFul.add(it)

                "Entertainment" -> enjoy.add(it)
                "Outfit" -> enjoy.add(it)
                "Travel" -> enjoy.add(it)
                "Beautify" -> enjoy.add(it)
                "Party" -> enjoy.add(it)

                "Gift" -> offering.add(it)
                "Charity" -> offering.add(it)

                "Doctor" -> health.add(it)
                "Sport" -> health.add(it)
                "Insurance" -> health.add(it)

                "Child" -> child.add(it)

                "Fees" -> other.add(it)
                "Drop" -> other.add(it)

                else -> {}
            }
        }
    }

    private fun initPieData(transactions: ArrayList<TransactionModel>): ArrayList<PieEntry> {
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(calculatePercentage(needFul, transactions), "NeedFul"))
        data.add(PieEntry(calculatePercentage(enjoy, transactions), "Enjoy"))
        data.add(PieEntry(calculatePercentage(offering, transactions), "Offering"))
        data.add(PieEntry(calculatePercentage(health, transactions), "Health"))
        data.add(PieEntry(calculatePercentage(child, transactions), "Child"))
        data.add(PieEntry(calculatePercentage(other, transactions), "Other"))
        cleanPercentageJunkData(data)
        return data
    }

    /*
    * Why cast here?
    * - because lis.size : Int and transactions.size : Int (Int / Int = Int (result = 0))
    * */
    private fun calculatePercentage(
        list : ArrayList<TransactionModel>, transactions : ArrayList<TransactionModel>
    ): Float {
        return (list.size.toFloat() / transactions.size.toFloat()) * 100
    }

    /*
    * If current wallet has not transaction, then category list at above is null.
    * From there, it will calculate the junk data, so we need to clean it
    * */
    private fun cleanPercentageJunkData(data: ArrayList<PieEntry>) {
        var count = 0
        for (entry in data) {
            if (entry.x == 0.0f && entry.y.isNaN()) { count++ }
        }
        if (count == data.size) data.clear()
    }

}