package com.kindsundev.expense.manager.ui.home.report

import com.github.mikephil.charting.data.Entry
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportPresenter(
    private val view: ReportContract.View
): ReportContract.Presenter {

    fun getBalanceHistorySevenDays(wallet: WalletModel): ArrayList<Entry> {
        val mBalanceInLastSevenDays = ArrayList<Entry>()
        val balanceHistory = wallet.calculateBalanceEveryDayForLastSevenDays()
        if (balanceHistory.isNotEmpty()) {
            balanceHistory.forEachIndexed { index, value ->
                mBalanceInLastSevenDays.add(Entry(index.toFloat(), value.toFloat()))
            }
        }
        return mBalanceInLastSevenDays
    }



}