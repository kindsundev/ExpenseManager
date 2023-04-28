package com.kindsundev.expense.manager.ui.home.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentReportBinding
import com.kindsundev.expense.manager.ui.home.report.chart.MyBarChart
import com.kindsundev.expense.manager.ui.home.report.chart.MyLineChart
import com.kindsundev.expense.manager.ui.home.report.chart.MyPieChart
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletContract
import com.kindsundev.expense.manager.utils.*

class ReportFragment : Fragment(), ReportContract.View {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding
    private lateinit var reportPresenter: ReportPresenter
    private lateinit var mWalletBottomSheet: ReportWalletBottomSheet

    private lateinit var mAssetsIOBarChart: BarChart
    private lateinit var mIncomePieChart: PieChart
    private lateinit var mExpensePieChart: PieChart
    private lateinit var mBalanceHistoryLineChart: LineChart

    private lateinit var assetsIOChart: MyBarChart
    private lateinit var incomeChart: MyPieChart
    private lateinit var expenseChart: MyPieChart
    private lateinit var balanceHistoryChart: MyLineChart

    private lateinit var balanceLegend: String
    private lateinit var sevenDayLabels: List<String>
    private lateinit var assetsLabels: Array<String>

    override fun getCurrentContext(): Context = requireContext()

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
        getStringLabelValue()
        mappingViews()
        initIncomeAndExpenseBarChart()
        initIncomePieChart()
        initExpensePieChart()
        initBalanceHistoryChart()
        initListener()
        return binding!!.root
    }

    private fun getStringLabelValue() {
        balanceLegend = requireContext().getString(R.string.balance)
        sevenDayLabels = requireContext().resources.getStringArray(R.array.seven_day).toList()
        assetsLabels = arrayOf(
            requireContext().getString(R.string.income),
            requireContext().getString(R.string.expense)
        )
    }

    private fun mappingViews() {
        mAssetsIOBarChart = binding!!.barChart.incomeAndExpense
        mBalanceHistoryLineChart = binding!!.lineChart.balance
        mExpensePieChart = binding!!.ePieChart.expense
        mIncomePieChart = binding!!.iPieChart.income
    }

    private fun initIncomeAndExpenseBarChart() {
        assetsIOChart = MyBarChart(mAssetsIOBarChart, incomeAndExpenseDataDefault(), assetsLabels)
        assetsIOChart.showBarChart()
    }

    private fun initIncomePieChart() {
        incomeChart =
            MyPieChart(
                getCurrentContext(),
                mIncomePieChart,
                incomePieDataDefault(getCurrentContext()),
                Constant.TRANSACTION_TYPE_INCOME
            )
        incomeChart.showPieChart()
    }

    private fun initExpensePieChart() {
        expenseChart =
            MyPieChart(
                getCurrentContext(),
                mExpensePieChart,
                expensePieDataDefault(getCurrentContext()),
                Constant.TRANSACTION_TYPE_EXPENSE
            )
        expenseChart.showPieChart()
    }

    private fun initBalanceHistoryChart() {
        balanceHistoryChart = MyLineChart(
            mBalanceHistoryLineChart,
            balanceHistoryDataDefault(),
            sevenDayLabels,
            balanceLegend
        )
        balanceHistoryChart.showLineChart()
    }

    private fun initListener() {
        binding!!.selectWallet.itemWallet.setOnClickListener { onClickSelectWallet() }
    }

    private fun onClickSelectWallet() {
        mWalletBottomSheet = ReportWalletBottomSheet(object : ReportWalletContract.Listener {
            override fun onClickWalletItem(wallet: WalletModel) {
                requestNewChartData(wallet)
                binding!!.selectWallet.tvWalletName.text = wallet.name
                mWalletBottomSheet.dismiss()
            }
        })
        mWalletBottomSheet.show(
            parentFragmentManager,
            Constant.REPORT_WALLET_BOTTOM_SHEET_WALLET_NAME
        )
    }

    private fun requestNewChartData(wallet: WalletModel) {
        reportPresenter.calculateTotalAmountOfIncomeAndExpense(wallet)
        reportPresenter.calculatePercentageInIncome(wallet)
        reportPresenter.calculatePercentageInExpense(wallet)
        reportPresenter.calculateBalanceHistoryLastSevenDays(wallet)
    }

    override fun showNewAssetsInAndOutChart(result: ArrayList<BarEntry>) {
        assetsIOChart = MyBarChart(mAssetsIOBarChart, result, assetsLabels)
        assetsIOChart.showBarChart()
        if (result.isEmpty()) {
            binding!!.barChart.incomeAndExpense.visibility = View.GONE
            binding!!.barChart.tvNoData.visibility = View.VISIBLE
        } else {
            binding!!.barChart.incomeAndExpense.visibility = View.VISIBLE
            binding!!.barChart.tvNoData.visibility = View.GONE
        }
    }

    override fun showNewPercentageIncomeChart(result: ArrayList<PieEntry>) {
        incomeChart = MyPieChart(
            getCurrentContext(),
            mIncomePieChart,
            result,
            Constant.TRANSACTION_TYPE_INCOME
        )
        incomeChart.showPieChart()
        checkAndChangePieLayout(
            data = result,
            chart = binding!!.iPieChart.income,
            message = binding!!.iPieChart.tvNoData
        )
    }

    private fun checkAndChangePieLayout(
        data: ArrayList<PieEntry>,
        chart: PieChart,
        message: TextView
    ) {
        if (data.isEmpty()) {
            chart.visibility = View.GONE
            message.visibility = View.VISIBLE
        } else {
            chart.visibility = View.VISIBLE
            message.visibility = View.GONE
        }
    }

    override fun showNewPercentageExpenseChart(result: ArrayList<PieEntry>) {
        expenseChart = MyPieChart(
            getCurrentContext(),
            mExpensePieChart,
            result,
            Constant.TRANSACTION_TYPE_EXPENSE
        )
        expenseChart.showPieChart()
        checkAndChangePieLayout(
            data = result,
            chart = binding!!.ePieChart.expense,
            message = binding!!.ePieChart.tvNoData
        )
    }

    override fun showNewBalanceHistoryChart(result: ArrayList<Entry>) {
        balanceHistoryChart =
            MyLineChart(mBalanceHistoryLineChart, result, sevenDayLabels, balanceLegend)
        balanceHistoryChart.showLineChart()
        if (result.isEmpty()) {
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