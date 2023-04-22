package com.kindsundev.expense.manager.ui.home.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentReportBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.report.chart.MyLineChart
import com.kindsundev.expense.manager.ui.home.report.chart.MyPieChart
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletContract
import com.kindsundev.expense.manager.utils.*

class ReportFragment : Fragment(), ReportContract.View {
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var reportPresenter: ReportPresenter
    private lateinit var mWalletBottomSheet: ReportWalletBottomSheet
    private lateinit var mWallet : WalletModel

    private lateinit var mIncomeAndExpenseBarChart: BarChart
    private lateinit var mBalanceHistoryLineChart: LineChart
    private lateinit var mExpensePieChart: PieChart
    private lateinit var mIncomePieChart: PieChart

    private lateinit var balanceHistoryChart: MyLineChart
    private lateinit var expenseChart: MyPieChart
    private lateinit var incomeChart: MyPieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportPresenter = ReportPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        mappingViews()
        initIncomeAndBalanceBarChart()
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

    private fun initIncomeAndBalanceBarChart() {
        val barDataIncome = BarDataSet(incomeBarData(), "Income")
        customizeValueInBarChart(barDataIncome, "Income")
        val barDataExpense = BarDataSet(expenseBarData(), "Expense")
        customizeValueInBarChart(barDataExpense, "Expense")

        val barData = BarData()
        barData.addDataSet(barDataIncome)
        barData.addDataSet(barDataExpense)

        mIncomeAndExpenseBarChart.data = barData
        mIncomeAndExpenseBarChart.invalidate()
    }

    private fun customizeValueInBarChart(data : BarDataSet, label: String) {
        data.formLineWidth = 2.5f
        data.valueTextSize = 10f
        if (label == "Income") {
            data.color = Color.parseColor(Constant.GREEN_COLOR_CODE)
        } else if (label == "Expense") {
            data.color = Color.parseColor(Constant.RED_COLOR_CODE)
        }
    }

    private fun initIncomePieChart() {
        incomeChart = MyPieChart(mIncomePieChart, incomePieDataDefault(), Constant.TRANSACTION_STATE_INCOME)
        incomeChart.showPieChart()
    }

    private fun initExpensePieChart() {
        expenseChart = MyPieChart(mExpensePieChart, expensePieDataDefault(), Constant.TRANSACTION_STATE_EXPENSE)
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
                updateExpenseChart()
                updateBalanceHistoryChart()
            }
        })
        mWalletBottomSheet.show(parentFragmentManager, Constant.REPORT_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    private fun updateExpenseChart() {
        val newData = reportPresenter.getPercentageInCategory(mWallet)
        expenseChart = MyPieChart(mExpensePieChart, newData, Constant.TRANSACTION_STATE_EXPENSE)
        expenseChart.showPieChart()
    }

    private fun updateBalanceHistoryChart() {
        val newData = reportPresenter.getBalanceHistorySevenDays(mWallet)
        balanceHistoryChart = MyLineChart(mBalanceHistoryLineChart, newData)
        balanceHistoryChart.showLineChart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoad() {
        TODO("Not yet implemented")
    }

    override fun onError(message: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }
}