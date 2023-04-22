package com.kindsundev.expense.manager.ui.home.report.chart

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class BalanceHistoryChart(
    private val lineChart: LineChart,
    private val balanceData: ArrayList<Entry>
) {

    private fun initLineDataSet(): LineDataSet {
        val dataSet= LineDataSet(balanceData, "Balance")
        dataSet.lineWidth = 2.5f
        dataSet.valueTextSize = 10f
        dataSet.circleRadius = 6f
        dataSet.circleHoleRadius = 3f
        return dataSet
    }

    private fun initLineData(): LineData {
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(initLineDataSet())
        return LineData(dataSet)
    }

    private fun configLabelAtXAxis() {
        val labels = listOf("Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7", )
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 0.5f
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < labels.size) {
                        labels[index]
                    } else {
                        ""
                    }
                }
            }
        }
    }

    private fun configLineChart() {
        configLabelAtXAxis()
        val mDescription = Description()
        mDescription.text = ""

        lineChart.apply {
            setExtraOffsets(0f,0f,0f,10f)
            axisRight.isEnabled = false
            legend.xOffset = -10f
            description = mDescription
        }
    }

    fun showLineChart() {
        configLineChart()
        lineChart.data = initLineData()
        if (lineChart.data == null || lineChart.data.dataSets.isEmpty() || lineChart.data.dataSets[0].entryCount <= 0) {
            lineChart.setNoDataText("This wallet has no balance fluctuations")
        }
        lineChart.invalidate()
    }

}