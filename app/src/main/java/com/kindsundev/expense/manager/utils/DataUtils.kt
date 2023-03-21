package com.kindsundev.expense.manager.utils

import android.util.Patterns
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

fun getCurrentTime(): String {
    return LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        .toString().trim()
}

fun hashCodeForID(vararg data: String): Int {
    var result = 0
    for (item in data) {
        result += item.hashCode()
    }
    return result
}
