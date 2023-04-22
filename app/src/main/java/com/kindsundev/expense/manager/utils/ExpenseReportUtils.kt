package com.kindsundev.expense.manager.utils

import com.github.mikephil.charting.data.PieEntry
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
            Logger.warn(billModel.date.toString())
            billModel.transactions?.forEach { transaction ->
                if (transaction.type == "expense") {
                    transactions.add(transaction)
                    Logger.info(transaction.toString())
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
        Logger.error("needFul  = ${needFul.size}")
        Logger.error("enjoy    = ${enjoy.size}")
        Logger.error("offering = ${offering.size}")
        Logger.error("health   = ${health.size}")
        Logger.error("child    = ${child.size}")
        Logger.error("other    = ${other.size}")
    }

    private fun initPieData(transactions: ArrayList<TransactionModel>): ArrayList<PieEntry> {
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(calculatePercentage(needFul, transactions), "NeedFul"))
        data.add(PieEntry(calculatePercentage(enjoy, transactions), "Enjoy"))
        data.add(PieEntry(calculatePercentage(offering, transactions), "Offering"))
        data.add(PieEntry(calculatePercentage(health, transactions), "Health"))
        data.add(PieEntry(calculatePercentage(child, transactions), "Child"))
        data.add(PieEntry(calculatePercentage(other, transactions), "Other"))
        return data
    }

    /*
    * why cast here?
    * - because lis.size : Int and transactions.size : Int
    * => Int / Int = Int (result = 0)
    * */
    private fun calculatePercentage(
        list : ArrayList<TransactionModel>, transactions : ArrayList<TransactionModel>
    ): Float {
        return (list.size.toFloat() / transactions.size.toFloat()) * 100
    }
}