package com.kindsundev.expense.manager.ui.home.report

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.data.model.WalletModel

interface ReportContract {

    interface Presenter {
        fun getBalanceHistorySevenDays(wallet: WalletModel): ArrayList<Entry>

        fun getPercentageInCategory(wallet: WalletModel, name: String): ArrayList<PieEntry>

        fun getTotalAmountOfIncomeAndExpense(wallet: WalletModel): ArrayList<BarEntry>
    }
}