package com.kindsundev.expense.manager.utils

import android.util.Patterns
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

fun checkEmailAndPassword(email: String, password: String): Status {
    return if (email.isEmpty()) {
        Status.WRONG_EMAIL_EMPTY
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Status.WRONG_EMAIL_PATTERN
    } else if (password.isEmpty()) {
        Status.WRONG_PASSWORD_EMPTY
    } else if (password.length < 6 && email.isNotEmpty()) {
        Status.WRONG_PASSWORD_LENGTH
    } else if (email.isEmpty() && password.isEmpty()) {
        Status.WRONG_EMAIL_PASSWORD_EMPTY
    } else {
        Status.VALID_DATA
    }
}

fun checkName(name: String): Status {
    return if (name.isEmpty()) {
        Status.WRONG_NAME_EMPTY
    } else if (isAShortName(name)) {
        Status.WRONG_NAME_SHORT
    } else if (isALongName(name)) {
        Status.WRONG_NAME_LONG
    } else if (isNumeric(name)) {
        Status.WRONG_NAME_HAS_DIGITS
    } else if (isSpecialCharacter(name)) {
        Status.WRONG_HAS_SPECIAL_CHARACTER
    } else {
        Status.VALID_DATA
    }
}

private fun isAShortName(name: String) = name.length < 3

private fun isALongName(name: String) = name.length > 70

private fun isNumeric(toCheck: String) = toCheck.all { char -> char.isDigit() }

private fun isSpecialCharacter(toCheck: String): Boolean {
    val matcher = Pattern.compile(Constant.SPECIAL_CHARACTERS).matcher(toCheck)
    return matcher.find()
}

fun checkEmail(email: String): Status {
    return if (email.isEmpty()) {
        Status.WRONG_EMAIL_EMPTY
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Status.WRONG_EMAIL_PATTERN
    } else {
        Status.VALID_DATA
    }
}

fun checkPassword(password: String): Status {
    return if (password.isEmpty()) {
        Status.WRONG_PASSWORD_EMPTY
    } else if (password.length < 6) {
        Status.WRONG_PASSWORD_LENGTH
    } else {
        Status.VALID_DATA
    }
}

fun checkBalance(balance: String): Status  {
    return if (balance.isEmpty()){
        Status.WRONG_BALANCE_EMPTY
    } else if(balance == ".") {
        Status.WRONG_HAS_SPECIAL_CHARACTER
    } else {
        Status.VALID_DATA
    }
}

fun getCurrentDate(): String {
    return LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd EEEE MMMM yyyy"))
        .toString().trim()
}

fun getCurrentDateTime(): String {
    return LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy"))
}

fun hashCodeForID(vararg data: String): Int {
    var result = 0
    for (item in data) {
        result += item.hashCode()
    }
    return result
}


// ---------------------------- TEMP DATE FOR UI ----------------------------

// Income vs Expense Chart
fun incomeBarData(): ArrayList<BarEntry> {
    val data = ArrayList<BarEntry>()
    data.add(BarEntry(0F, 3F))
    data.add(BarEntry(3F, 6F))
    data.add(BarEntry(6F, 2F))
    data.add(BarEntry(9F, 4F))
    return data
}

fun expenseBarData() : ArrayList<BarEntry> {
    val data = ArrayList<BarEntry>()
    data.add(BarEntry(1F, 2F))
    data.add(BarEntry(4F, 5F))
    data.add(BarEntry(7F, 3F))
    data.add(BarEntry(10F, 7F))
    return data
}

// Income Chart
fun incomePieData(): ArrayList<PieEntry> {
    val data = ArrayList<PieEntry>()
    data.add(PieEntry(30F, "NeedFul"))
    data.add(PieEntry(15F, "Enjoy"))
    data.add(PieEntry(10F, "Offering"))
    data.add(PieEntry(10F, "Health"))
    data.add(PieEntry(30F, "Child"))
    data.add(PieEntry(5F, "Other"))
    return data
}

// Expense Chart
fun expensePieData(): ArrayList<PieEntry> {
    val data = ArrayList<PieEntry>()
    data.add(PieEntry(70F, "Salary"))
    data.add(PieEntry(5F, "Bons"))
    data.add(PieEntry(20F, "Interest Rate"))
    data.add(PieEntry(5F, "Other"))
    return data
}

// Balance History Chart
fun balanceHistoryData(): ArrayList<Entry> {
    val data = ArrayList<Entry>()
    data.add(Entry(0F, 0F))
    data.add(Entry(1F, 100F))
    data.add(Entry(2F, 80F))
    data.add(Entry(3F, 60F))
    data.add(Entry(4F, 120F))
    data.add(Entry(5F, 30F))
    data.add(Entry(6F, 50F))
    return data
}