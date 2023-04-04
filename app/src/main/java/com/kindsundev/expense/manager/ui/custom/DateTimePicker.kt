package com.kindsundev.expense.manager.ui.custom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.time.LocalDateTime
import kotlin.properties.Delegates

class DateTimePicker(
    private val context: Context,
    private val listener: CallbackDateTime
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

    private fun getCurrentDateTimeCalender() {
        val rightNow = LocalDateTime.now()
        currentDay = rightNow.dayOfMonth
        currentMonth = rightNow.monthValue
        currentYear = rightNow.year
        currentHour = rightNow.hour
        currentMinute = rightNow.minute
    }

    fun onShowDateTimePickerDialog() {
        getCurrentDateTimeCalender()
        DatePickerDialog(context, this, currentYear, currentMonth, currentDay).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        newDay = dayOfMonth
        newMonth = month
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