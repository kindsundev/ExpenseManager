package com.kindsundev.expense.manager.utils

import com.kindsundev.expense.manager.data.model.BillModel

class TotalReportUtils {

    fun calculateTotalExpense(bills: ArrayList<BillModel>): Double {
        var sum = 0.0
        bills.forEach { billModel ->
            sum += billModel.calculateTotalExpenseForDay()
        }
        return sum
    }

    fun calculateTotalIncome(bills: ArrayList<BillModel>): Double {
        var sum = 0.0
        bills.forEach { billModel ->
            sum += billModel.calculateTotalIncomeForDay()
        }
        return sum
    }
}