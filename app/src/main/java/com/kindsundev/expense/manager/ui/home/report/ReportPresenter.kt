package com.kindsundev.expense.manager.ui.home.report

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.utils.BalanceReportUtils
import com.kindsundev.expense.manager.utils.ExpenseReportUtils
import com.kindsundev.expense.manager.utils.IncomeReportUtils
import com.kindsundev.expense.manager.utils.TotalReportUtils
import kotlinx.coroutines.*

class ReportPresenter(
    private val view: ReportContract.View
): ReportContract.Presenter {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val expenseReport = ExpenseReportUtils(view.getCurrentContext())
    private val incomeReport = IncomeReportUtils(view.getCurrentContext())
    private val balanceReport = BalanceReportUtils()
    private val totalReport = TotalReportUtils()

    override fun calculateTotalAmountOfIncomeAndExpense(wallet: WalletModel) {
        scope.launch {
            val bills = wallet.getBills()
            val totalIncome = totalReport.calculateTotalIncome(bills)
            val totalExpense = totalReport.calculateTotalExpense(bills)
            val totalAmountEntries = initTotalAmountOfEachType(totalIncome, totalExpense)
            withContext(Dispatchers.Main) {
                view.showNewAssetsInAndOutChart(totalAmountEntries)
            }
        }
    }

    private fun initTotalAmountOfEachType(totalIncome: Double, totalExpense: Double): ArrayList<BarEntry> {
        val list = ArrayList<BarEntry>()
        list.add(BarEntry(0F, totalIncome.toFloat()))
        list.add(BarEntry(1F, totalExpense.toFloat()))
        if (totalExpense == 0.0 && totalIncome == 0.0) { list.clear() }
        return list
    }

    override fun calculatePercentageInIncome(wallet: WalletModel) {
        scope.launch {
            val incomePercentageEntries = incomeReport.getPercentage(wallet.getBills())
            cleanDataForPieChart(incomePercentageEntries)
            withContext(Dispatchers.Main) {
                view.showNewPercentageIncomeChart(incomePercentageEntries)
            }
        }
    }

    private fun cleanDataForPieChart(data: ArrayList<PieEntry>) {
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value == 0.0f) {
                iterator.remove()
            }
        }
    }

    override fun calculatePercentageInExpense(wallet: WalletModel) {
        scope.launch {
            val expensePercentageEntries = expenseReport.getPercentage(wallet.getBills())
            cleanDataForPieChart(expensePercentageEntries)
            withContext(Dispatchers.Main) {
                view.showNewPercentageExpenseChart(expensePercentageEntries)
            }
        }
    }

    override fun calculateBalanceHistoryLastSevenDays(wallet: WalletModel) {
        scope.launch {
            val currentBalance = wallet.balance
            val bills = wallet.getBills()
            val balanceList = balanceReport.calculateBalanceEveryDayForLastSevenDays(currentBalance!!, bills)
            val balanceEntries = initBalanceEntries(balanceList)
            withContext(Dispatchers.Main) {
                view.showNewBalanceHistoryChart(balanceEntries)
            }
        }
    }

    private fun initBalanceEntries(data: ArrayList<Double>): ArrayList<Entry> {
        val list = ArrayList<Entry>()
        data.forEachIndexed { index, value ->
            list.add(Entry(index.toFloat(), value.toFloat()))
        }
        return list
    }
}