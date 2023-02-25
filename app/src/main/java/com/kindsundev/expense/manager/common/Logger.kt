package com.kindsundev.expense.manager.common

import android.util.Log

object Logger {
    private const val TAG = "debug"

    fun error(message: String) {
        Log.e(TAG, message)
    }

    fun info(message: String) {
        Log.i(TAG, message)
    }

    fun warn(message: String) {
        Log.w(TAG, message)
    }
}