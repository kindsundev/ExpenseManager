package com.kindsundev.expense.manager.ui.home.report

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.utils.BalanceReportUtils
import com.kindsundev.expense.manager.utils.ExpenseReportUtils
import com.kindsundev.expense.manager.utils.IncomeReportUtils
import com.kindsundev.expense.manager.utils.TotalReportUtils
import kotlin.collections.ArrayList

class ReportPresenter: ReportContract.Presenter {
    private val balanceReport = BalanceReportUtils()
    private val expenseReport = ExpenseReportUtils()
    private val incomeReport = IncomeReportUtils()
    private val totalReport = TotalReportUtils()

    override fun getBalanceHistorySevenDays(wallet: WalletModel): ArrayList<Entry> {
        val mBalanceInLastSevenDays = ArrayList<Entry>()
        val balanceHistory = balanceReport.calculateBalanceEveryDayForLastSevenDays(
            wallet.balance!!,
            wallet.getBills()
        )
        if (balanceHistory.isNotEmpty()) {
            balanceHistory.forEachIndexed { index, value ->
                mBalanceInLastSevenDays.add(Entry(index.toFloat(), value.toFloat()))
            }
        }
        return mBalanceInLastSevenDays
    }

    override fun getPercentageInCategory(wallet: WalletModel, name: String): ArrayList<PieEntry> {
        var result =  ArrayList<PieEntry>()
        if (name == Constant.TRANSACTION_TYPE_EXPENSE) {
            result =  expenseReport.getPercentage(wallet.getBills())
            result.forEach { Logger.warn(it.toString()) }
        } else if (name == Constant.TRANSACTION_TYPE_INCOME) {
            result =  incomeReport.getPercentage(wallet.getBills())
            result.forEach { Logger.error(it.toString()) }
        }
        cleanDataForPieChart(result)
        return result
    }

    // ConcurrentModificationException when loop by for
    private fun cleanDataForPieChart(data: ArrayList<PieEntry>) {
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value == 0.0f) {
                iterator.remove()
            }
        }
    }

    override fun getTotalAmountOfIncomeAndExpense(wallet: WalletModel): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val bills = wallet.getBills()
        val totalIncome = totalReport.calculateTotalIncome(bills)
        val totalExpense = totalReport.calculateTotalExpense(bills)
        data.add(BarEntry(0F, totalIncome.toFloat()))
        data.add(BarEntry(1F, totalExpense.toFloat()))
        if (totalExpense == 0.0 && totalIncome == 0.0) { data.clear() }
        return data
    }

}