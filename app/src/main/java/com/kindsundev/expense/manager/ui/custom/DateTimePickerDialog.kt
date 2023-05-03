package com.kindsundev.expense.manager.ui.custom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.time.LocalDateTime
import kotlin.properties.Delegates

class DateTimePickerDialog(
    private val context: Context,
    private val listener: ResultDateTimeCallback
) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var currentDay by Delegates.notNull<Int>()
    private var currentMonth by Delegates.notNull<Int>()
    private var currentYear by Delegates.notNull<Int>()
    private var currentHour by Delegates.notNull<Int>()
    private var currentMinute by Delegates.notNull<Int>()

    private var newDay by Delegates.notNull<Int>()
    private var newMonth by Delegates.notNull<Int>()
    private var newYear by Delegates.notNull<Int>()
    private var newHour by Delegates.notNull<Int>()
    private var newMinute by Delegates.notNull<Int>()

    private var newDateTime: String = "Null"

    fun onShowDateTimePickerDialog() {
        getCurrentDateTimeCalender()
        DatePickerDialog(context, this, currentYear, currentMonth, currentDay).show()
    }

    private fun getCurrentDateTimeCalender() {
        val rightNow = LocalDateTime.now()
        currentDay = rightNow.dayOfMonth
        currentMonth = rightNow.monthValue - 1
        currentYear = rightNow.year
        currentHour = rightNow.hour
        currentMinute = rightNow.minute
    }

    /*
    * Why me handle (-1) & (+1) here?
    * - DatePickerDialog: [month] range [0-11] not [1-12]
    * - LocalDateTime: [month] range [1-12]
    * -> LocalDateTime: [month - 1] then Result: [month + 1]
    *
    * (If not handle as so,
    * then it is currently May but when displayed as June,
    * then select June but the result is still May.
    * This means data inconsistency between data and client)
    * */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        newDay = dayOfMonth
        newMonth = month + 1
        newYear = year
        getCurrentDateTimeCalender()
        TimePickerDialog(context, this, currentHour, currentMinute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        newHour = hourOfDay
        newMinute = minute
        newDateTime =  "$newHour:$newMinute, $newDay-$newMonth-$newYear"
        listener.resultNewDateTime(newDateTime)
    }
}