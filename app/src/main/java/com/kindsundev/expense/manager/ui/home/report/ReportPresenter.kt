package com.kindsundev.expense.manager.ui.home.report

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.utils.BalanceReportUtils
import com.kindsundev.expense.manager.utils.ExpenseReportUtils
import kotlin.collections.ArrayList

class ReportPresenter(
    private val view: ReportContract.View
): ReportContract.Presenter {
    private val balanceReport = BalanceReportUtils()
    private val expenseReport = ExpenseReportUtils()

    fun getBalanceHistorySevenDays(wallet: WalletModel): ArrayList<Entry> {
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

    // ConcurrentModificationException when loop by for
    fun getPercentageInCategory(wallet: WalletModel): ArrayList<PieEntry> {
        val result =  expenseReport.getPercentage(wallet.getBills())
        val iterator = result.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value == 0.0f) {
                iterator.remove()
            }
        }
        return result
    }


}