package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

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

    fun getBillsOfPlan(planId: Int): ArrayList<BillModel> {
        val bills = ArrayList<BillModel>()
        val uniqueDates = HashSet<String>()
        transactions?.let {
            for ((date, map) in it) {
                val transactions = ArrayList<TransactionModel>()
                for ((_, model) in map) {
                    if (model.planId == planId) {
                        transactions.add(model)
                    }
                    if (transactions.isNotEmpty() && date !in uniqueDates) {
                        bills.add(BillModel(date, transactions))
                        uniqueDates.add(date)
                    }
                }
            }
        }
        return bills
    }

    fun sortBillsByNewest(data: ArrayList<BillModel>): List<BillModel> {
        var list : List<BillModel> = listOf()
        if (data.isNotEmpty()) {
            list = data.sortedByDescending { bill ->
                bill.date?.let {
                    SimpleDateFormat("dd E MMMM yyyy", Locale.ENGLISH).parse(it)
                }
            }
        }
        return list
    }

    fun getPlannedList(): ArrayList<PlannedModel> {
        val list = ArrayList<PlannedModel>()
        plans?.let {
            for ((date, map) in it) {
                for ((_, model) in map) {
                    list.add(PlannedModel(date, model))
                }
            }
        }
        return list
    }

    fun getPlanned(planId: Int?): PlannedModel {
        var planned = PlannedModel()
        plans?.let {
            for ((date, map) in it) {
                for ((_, model) in map) {
                    if (model.id == planId) {
                        planned = PlannedModel(date, model)
                        break
                    }
                }
            }
        }
        return planned
    }
}