package com.kindsundev.expense.manager.ui.home.report.chart

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class MyLineChart(
    private val lineChart: LineChart,
    private val data: ArrayList<Entry>,
    private val labels: List<String>,
    private val legend: String
) {
    private val largeValueFormatter = LargeValueFormatter()

    private fun initLineDataSet(): LineDataSet {
        val dataSet = LineDataSet(data, legend)
        dataSet.apply {
            lineWidth = 2.5f
            valueTextSize = 10f
            circleRadius = 6f
            circleHoleRadius = 3f
            valueFormatter = largeValueFormatter
        }
        return dataSet
    }

    private fun initLineData(): LineData {
        val lineData = ArrayList<ILineDataSet>()
        lineData.add(initLineDataSet())
        return LineData(lineData)
    }

    private fun configLabelAtXAxis() {
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

    fun showLineChart() {
        configLabelAtXAxis()
        lineChart.apply {
            setExtraOffsets(0f,0f,0f,10f)
            axisLeft.valueFormatter = largeValueFormatter
            axisRight.isEnabled = false
            legend.xOffset = -10f
            description.isEnabled = false
            animateXY(700, 700)
            lineChart.data = initLineData()
        }
        lineChart.invalidate()
    }

}