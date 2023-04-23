package com.kindsundev.expense.manager.ui.home.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentReportBinding
import com.kindsundev.expense.manager.ui.home.report.chart.MyBarChart
import com.kindsundev.expense.manager.ui.home.report.chart.MyLineChart
import com.kindsundev.expense.manager.ui.home.report.chart.MyPieChart
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletContract
import com.kindsundev.expense.manager.utils.*

class ReportFragment : Fragment(){
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding

    private lateinit var reportPresenter: ReportPresenter
    private lateinit var mWalletBottomSheet: ReportWalletBottomSheet
    private lateinit var mWallet : WalletModel

    private lateinit var mIncomeAndExpenseBarChart: BarChart
    private lateinit var mIncomePieChart: PieChart
    private lateinit var mExpensePieChart: PieChart
    private lateinit var mBalanceHistoryLineChart: LineChart

    private lateinit var totalChart: MyBarChart
    private lateinit var incomeChart: MyPieChart
    private lateinit var expenseChart: MyPieChart
    private lateinit var balanceHistoryChart: MyLineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportPresenter = ReportPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        mappingViews()
        initIncomeAndExpenseBarChart()
        initIncomePieChart()
        initExpensePieChart()
        initBalanceHistoryChart()
        initListener()
        return binding!!.root
    }

    private fun mappingViews() {
        mIncomeAndExpenseBarChart = binding!!.barChart.incomeAndExpense
        mBalanceHistoryLineChart = binding!!.lineChart.balance
        mExpensePieChart = binding!!.ePieChart.expense
        mIncomePieChart = binding!!.iPieChart.income
    }

    private fun initIncomeAndExpenseBarChart() {
        totalChart = MyBarChart(mIncomeAndExpenseBarChart, incomeAndExpenseDataDefault())
        totalChart.showBarChart()
    }

    private fun initIncomePieChart() {
        incomeChart = MyPieChart(mIncomePieChart, incomePieDataDefault(), Constant.TRANSACTION_TYPE_INCOME)
        incomeChart.showPieChart()
    }

    private fun initExpensePieChart() {
        expenseChart = MyPieChart(mExpensePieChart, expensePieDataDefault(), Constant.TRANSACTION_TYPE_EXPENSE)
        expenseChart.showPieChart()
    }

    private fun initBalanceHistoryChart() {
        balanceHistoryChart = MyLineChart(mBalanceHistoryLineChart, balanceHistoryDataDefault())
        balanceHistoryChart.showLineChart()
    }

    private fun initListener() {
        binding!!.selectWallet.itemWallet.setOnClickListener { onClickSelectWallet() }
    }

    private fun onClickSelectWallet() {
        mWalletBottomSheet = ReportWalletBottomSheet(object: ReportWalletContract.Listener {
            override fun onClickWalletItem(wallet: WalletModel) {
                mWallet = wallet
                binding!!.selectWallet.tvWalletName.text = mWallet.name
                mWalletBottomSheet.dismiss()
                updateIncomeAndExpenseChart()
                updateIncomeChart()
                updateExpenseChart()
                updateBalanceHistoryChart()
            }
        })
        mWalletBottomSheet.show(parentFragmentManager, Constant.REPORT_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    private fun updateIncomeAndExpenseChart() {
        val newData = reportPresenter.getTotalAmountOfIncomeAndExpense(mWallet)
        totalChart = MyBarChart(mIncomeAndExpenseBarChart, newData)
        totalChart.showBarChart()
        if (newData.isEmpty()) {
            binding!!.barChart.incomeAndExpense.visibility = View.GONE
            binding!!.barChart.tvNoData.visibility = View.VISIBLE
        } else {
            binding!!.barChart.incomeAndExpense.visibility = View.VISIBLE
            binding!!.barChart.tvNoData.visibility = View.GONE
        }
    }

    private fun updateIncomeChart() {
        val newData = reportPresenter.getPercentageInCategory(mWallet, Constant.TRANSACTION_TYPE_INCOME)
        incomeChart = MyPieChart(mIncomePieChart, newData, Constant.TRANSACTION_TYPE_INCOME)
        incomeChart.showPieChart()
        if (newData.isEmpty()) {
            binding!!.iPieChart.income.visibility = View.GONE
            binding!!.iPieChart.tvNoData.visibility = View.VISIBLE
        } else {
            binding!!.iPieChart.income.visibility = View.VISIBLE
            binding!!.iPieChart.tvNoData.visibility = View.GONE
        }
    }

    private fun updateExpenseChart() {
        val newData = reportPresenter.getPercentageInCategory(mWallet, Constant.TRANSACTION_TYPE_EXPENSE)
        expenseChart = MyPieChart(mExpensePieChart, newData, Constant.TRANSACTION_TYPE_EXPENSE)
        expenseChart.showPieChart()
        if (newData.isEmpty()) {
            binding!!.ePieChart.expense.visibility = View.GONE
            binding!!.ePieChart.tvNoData.visibility = View.VISIBLE
        } else {
            binding!!.ePieChart.expense.visibility = View.VISIBLE
            binding!!.ePieChart.tvNoData.visibility = View.GONE
        }
    }

    private fun updateBalanceHistoryChart() {
        val newData = reportPresenter.getBalanceHistorySevenDays(mWallet)
        balanceHistoryChart = MyLineChart(mBalanceHistoryLineChart, newData)
        balanceHistoryChart.showLineChart()
        if (newData.isEmpty()) {
            binding!!.lineChart.balance.visibility = View.GONE
            binding!!.lineChart.tvNoData.visibility = View.VISIBLE
        } else {
            binding!!.lineChart.balance.visibility = View.VISIBLE
            binding!!.lineChart.tvNoData.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}