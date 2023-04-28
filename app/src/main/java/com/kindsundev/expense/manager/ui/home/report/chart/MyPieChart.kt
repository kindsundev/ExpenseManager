package com.kindsundev.expense.manager.ui.home.report.chart

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.utils.expenseColorList
import com.kindsundev.expense.manager.utils.incomeColorList
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class MyPieChart(
    private val context: Context,
    private val pieChart: PieChart,
    private val data: ArrayList<PieEntry>,
    private val name: String
) {

    private fun initPieDataSet(): PieDataSet {
        val dataSet = PieDataSet(data, "")
        dataSet.apply {
            valueLineWidth = 2.5f
            valueTextSize = 10f
            valueTextColor = Color.WHITE
            if (name == Constant.TRANSACTION_TYPE_EXPENSE) {
                colors = expenseColorList(context, data)
            } else if (name == Constant.TRANSACTION_TYPE_INCOME) {
                colors = incomeColorList(context, data)
            }
        }
        return dataSet
    }

    private fun initPieData(): PieData {
        val pieData = PieData(initPieDataSet())
        pieData.apply {
            val percentFormat = DecimalFormat("###,###,##0.00'%'")
            setValueFormatter(object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return percentFormat.format(value)
                }
            })
        }
        return pieData
    }

    private fun configDescriptionLegendDisplay() {
        pieChart.legend.apply {
            isWordWrapEnabled = true
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)                        // show without chart
        }
    }

    fun showPieChart() {
        configDescriptionLegendDisplay()
        pieChart.apply {
            setDrawEntryLabels(false)
            description.isEnabled = false
            animateXY(700, 700)
            data = initPieData()
            invalidate()
        }
    }

}