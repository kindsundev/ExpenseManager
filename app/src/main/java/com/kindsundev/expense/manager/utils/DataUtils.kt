package com.kindsundev.expense.manager.utils

import android.util.Patterns
import com.kindsundev.expense.manager.common.Status

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
