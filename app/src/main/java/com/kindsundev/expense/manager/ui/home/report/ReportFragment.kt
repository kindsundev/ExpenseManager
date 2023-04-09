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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.FragmentReportBinding
import com.kindsundev.expense.manager.utils.*

class ReportFragment : Fragment() {
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding

    private lateinit var incomeAndExpenseBarChart: BarChart
    private lateinit var balanceHistoryLineChart: LineChart
    private lateinit var expensePieChart: PieChart
    private lateinit var incomePieChart: PieChart

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
        return binding!!.root
    }

    private fun mappingViews() {
        incomeAndExpenseBarChart = binding!!.barChart.incomeAndExpense
        balanceHistoryLineChart = binding!!.lineChart.balance
        expensePieChart = binding!!.ePieChart.expense
        incomePieChart = binding!!.iPieChart.income
    }

    private fun initIncomeAndBalanceBarChart() {
        val barDataIncome = BarDataSet(incomeBarData(), "Income")
        customizeValueInBarChart(barDataIncome, "Income")
        val barDataExpense = BarDataSet(expenseBarData(), "Expense")
        customizeValueInBarChart(barDataExpense, "Expense")

        val barData = BarData()
        barData.addDataSet(barDataIncome)
        barData.addDataSet(barDataExpense)

        incomeAndExpenseBarChart.data = barData
        incomeAndExpenseBarChart.invalidate()
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
        incomePieChart.data = pieData
        incomePieChart.setEntryLabelTextSize(10f)
        incomePieChart.invalidate()
    }

    private fun initExpensePieChart() {
        val pieDataSet = PieDataSet(expensePieData(), "")
        pieDataSet.valueLineWidth = 2.5f
        pieDataSet.valueTextSize = 10f
        pieDataSet.colors = formatColorList()

        val pieData = PieData(pieDataSet)
        expensePieChart.data = pieData
        expensePieChart.setEntryLabelTextSize(10f)
        expensePieChart.invalidate()
    }

    private fun initBalanceHistoryChart() {
        val lineData = LineDataSet(balanceHistoryData(), "Balance")
        lineData.lineWidth = 2.5f
        lineData.valueTextSize = 10f

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineData)
        val data = LineData(dataSets)

        balanceHistoryLineChart.data = data
        balanceHistoryLineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}