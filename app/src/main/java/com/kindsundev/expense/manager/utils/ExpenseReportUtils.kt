package com.kindsundev.expense.manager.utils

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel

class ExpenseReportUtils(private val context: Context) {
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
                context.getString(R.string.eat) -> needFul.add(it)
                context.getString(R.string.bill) -> needFul.add(it)
                context.getString(R.string.move) -> needFul.add(it)
                context.getString(R.string.house) -> needFul.add(it)
                context.getString(R.string.shopping) -> needFul.add(it)

                context.getString(R.string.entertainment) -> enjoy.add(it)
                context.getString(R.string.outfit) -> enjoy.add(it)
                context.getString(R.string.travel) -> enjoy.add(it)
                context.getString(R.string.beautify) -> enjoy.add(it)
                context.getString(R.string.party) -> enjoy.add(it)

                context.getString(R.string.gift) -> offering.add(it)
                context.getString(R.string.charity) -> offering.add(it)

                context.getString(R.string.doctor) -> health.add(it)
                context.getString(R.string.sport) -> health.add(it)
                context.getString(R.string.insurance) -> health.add(it)

                context.getString(R.string.child) -> child.add(it)

                context.getString(R.string.fees) -> other.add(it)
                context.getString(R.string.drop) -> other.add(it)

                else -> {}
            }
        }
    }

    private fun initPieData(transactions: ArrayList<TransactionModel>): ArrayList<PieEntry> {
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(calculatePercentage(needFul, transactions), context.getString(R.string.needful)))
        data.add(PieEntry(calculatePercentage(enjoy, transactions), context.getString(R.string.enjoy)))
        data.add(PieEntry(calculatePercentage(offering, transactions), context.getString(R.string.offering)))
        data.add(PieEntry(calculatePercentage(health, transactions), context.getString(R.string.health)))
        data.add(PieEntry(calculatePercentage(child, transactions), context.getString(R.string.child)))
        data.add(PieEntry(calculatePercentage(other, transactions), context.getString(R.string.other)))
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