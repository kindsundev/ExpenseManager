package com.kindsundev.expense.manager.ui.custom

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.widget.DatePicker
import java.time.LocalDateTime
import java.util.*
import kotlin.properties.Delegates

class DateSelectionDialog(
    private val context: Context,
    private val listener: ResultDateTimeCallback
) : DatePickerDialog.OnDateSetListener {
    private var currentDay by Delegates.notNull<Int>()
    private var currentMonth by Delegates.notNull<Int>()
    private var currentYear by Delegates.notNull<Int>()

    fun onShowDatePickerDialog() {
        getCurrentDateTimeCalender()
        DatePickerDialog(
            context,
            this,
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun getCurrentDateTimeCalender() {
        val rightNow = LocalDateTime.now()
        currentDay = rightNow.dayOfMonth
        currentMonth = rightNow.monthValue  - 1
        currentYear = rightNow.year
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val result = formatDate(year, month, dayOfMonth)
        listener.resultNewDateTime(result)
    }

    /*
    * calendar.setMonth() format zero-based index (0->1, 1->2...),
    * so we don't need to add 1 unit to the month
    * */
    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 0
        calendar.set(year, month, dayOfMonth)
        val date: Date = calendar.time
        return SimpleDateFormat("dd EEEE MMMM yyyy").format(date)
    }
}