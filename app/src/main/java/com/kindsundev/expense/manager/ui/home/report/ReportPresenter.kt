package com.kindsundev.expense.manager.ui.home.report

import com.github.mikephil.charting.data.Entry
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.utils.BalanceReportUtils
import kotlin.collections.ArrayList

class ReportPresenter(
    private val view: ReportContract.View
): ReportContract.Presenter {
    private val balanceReport = BalanceReportUtils()

    fun getBalanceHistorySevenDays(wallet: WalletModel): ArrayList<Entry> {
        val mBalanceInLastSevenDays = ArrayList<Entry>()
        val bills = wallet.getBills()
        val balanceHistory = balanceReport.calculateBalanceEveryDayForLastSevenDays(wallet.balance!!, bills)
        if (balanceHistory.isNotEmpty()) {
            balanceHistory.forEachIndexed { index, value ->
                mBalanceInLastSevenDays.add(Entry(index.toFloat(), value.toFloat()))
            }
        }
        return mBalanceInLastSevenDays
    }



}