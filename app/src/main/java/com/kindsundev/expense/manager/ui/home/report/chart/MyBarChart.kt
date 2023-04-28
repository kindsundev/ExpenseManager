package com.kindsundev.expense.manager.ui.home.report.chart

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.kindsundev.expense.manager.utils.incomeAndExpenseColorList

class MyBarChart(
    private val barChart: BarChart,
    private val data: ArrayList<BarEntry>,
    private val labels: Array<String>
) {
    private fun initBarDataSet(): BarDataSet {
        val dataSet = BarDataSet(data, "")
        dataSet.apply {
            valueTextSize = 10f
            colors = incomeAndExpenseColorList()
            valueFormatter = LargeValueFormatter()
        }
        return dataSet
    }

    private fun initBarData(): BarData  = BarData(initBarDataSet())

    private fun configXAxis() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            granularity = 1f
            labelCount = 2
            valueFormatter = IndexAxisValueFormatter(labels)
        }
    }

    private fun configYAxis() {
        barChart.axisLeft.apply {
            axisMinimum = 0f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
        }
        barChart.axisRight.apply {
            axisMinimum = 0f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
        }
    }

    fun showBarChart() {
        configXAxis()
        configYAxis()
        barChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawGridBackground(false)
            setFitBars(true)
            animateXY(700, 700)
            data = initBarData()
        }
        barChart.invalidate()
    }
}