package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import com.kindsundev.expense.manager.common.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillModel(
    val date: String? = "",
    var transactions: ArrayList<TransactionModel>? = ArrayList()
) : Parcelable {

    fun calculateBalanceForDay(): Double {
        var sumExpense = 0.0
        var sumIncome = 0.0
        transactions?.forEach { model ->
            if (model.type == Constant.TRANSACTION_TYPE_EXPENSE) {
                sumExpense += model.amount!!
            } else if (model.type == Constant.TRANSACTION_TYPE_INCOME) {
                sumIncome += model.amount!!
            }
        }
        return sumIncome - sumExpense
    }

    fun calculateTotalExpenseForDay(): Double {
        var sumExpense = 0.0
        transactions?.forEach { model ->
            if (model.type == Constant.TRANSACTION_TYPE_EXPENSE) {
                sumExpense += model.amount!!
            }
        }
        return sumExpense
    }

    fun calculateTotalIncomeForDay(): Double {
        var sumIncome = 0.0
        transactions?.forEach { model ->
            if (model.type == Constant.TRANSACTION_TYPE_INCOME) {
                sumIncome += model.amount!!
            }
        }
        return sumIncome
    }
}