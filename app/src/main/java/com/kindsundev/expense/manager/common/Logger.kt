package com.kindsundev.expense.manager.common

import android.util.Log

object Logger {
    private const val TAG = "debug"

    fun error(message: String) {
        Log.e(TAG, "Error: $message")
    }

    fun info(message: String) {
        Log.i(TAG, "Info: $message")
    }
}