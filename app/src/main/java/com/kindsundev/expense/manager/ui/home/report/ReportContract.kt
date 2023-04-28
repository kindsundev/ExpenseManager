package com.kindsundev.expense.manager.ui.home.report

import android.content.Context
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.data.model.WalletModel

interface ReportContract {

    interface Presenter {
        fun calculateTotalAmountOfIncomeAndExpense(wallet: WalletModel)

        fun calculatePercentageInIncome(wallet: WalletModel)

        fun calculatePercentageInExpense(wallet: WalletModel)

        fun calculateBalanceHistoryLastSevenDays(wallet: WalletModel)
    }

    interface View {
        fun getCurrentContext(): Context

        fun showNewAssetsInAndOutChart(result: ArrayList<BarEntry>)

        fun showNewPercentageIncomeChart(result: ArrayList<PieEntry>)

        fun showNewPercentageExpenseChart(result: ArrayList<PieEntry>)

        fun showNewBalanceHistoryChart(result: ArrayList<Entry>)
    }
}