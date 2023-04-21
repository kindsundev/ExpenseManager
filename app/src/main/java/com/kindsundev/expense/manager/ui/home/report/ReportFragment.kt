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
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentReportBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.report.wallet.ReportWalletContract
import com.kindsundev.expense.manager.utils.*


class ReportFragment : Fragment() {
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var mIncomeAndExpenseBarChart: BarChart
    private lateinit var mBalanceHistoryLineChart: LineChart
    private lateinit var mExpensePieChart: PieChart
    private lateinit var mIncomePieChart: PieChart

    private lateinit var mWalletBottomSheet: ReportWalletBottomSheet
    private lateinit var mWallet : WalletModel


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
        val pieDataSet = PieDataSet(incomePieData(), "")
        pieDataSet.valueLineWidth = 2.5f
        pieDataSet.valueTextSize = 10f
        pieDataSet.colors = formatColorList()

        val pieData = PieData(pieDataSet)
        mIncomePieChart.data = pieData
        mIncomePieChart.setEntryLabelTextSize(10f)
        mIncomePieChart.invalidate()
    }

    private fun initExpensePieChart() {
        val pieDataSet = PieDataSet(expensePieData(), "")
        pieDataSet.valueLineWidth = 2.5f
        pieDataSet.valueTextSize = 10f
        pieDataSet.colors = formatColorList()

        val pieData = PieData(pieDataSet)
        mExpensePieChart.data = pieData
        mExpensePieChart.setEntryLabelTextSize(10f)
        mExpensePieChart.invalidate()
    }

    private fun initBalanceHistoryChart() {
        val lineData = LineDataSet(balanceHistoryData(), "Balance")
        lineData.lineWidth = 2.5f
        lineData.valueTextSize = 10f
        lineData.circleRadius = 6f
        lineData.circleHoleRadius = 3f

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineData)
        val mData = LineData(dataSets)

        configLabelForBalanceHistoryChart()

        val mDescription = Description()
        mDescription.text = ""
        mBalanceHistoryLineChart.apply {
            setExtraOffsets(0f,0f,0f,10f)
            axisRight.isEnabled = false
            legend.xOffset = -10f
            description = mDescription
            data = mData
            invalidate()
        }
    }

    private fun configLabelForBalanceHistoryChart() {
        val labels = listOf("Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7")
        mBalanceHistoryLineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 0.5f
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return labels[value.toInt()]
                }
            }
        }
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
            }
        })
        mWalletBottomSheet.show(parentFragmentManager, Constant.REPORT_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}