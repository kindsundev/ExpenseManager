package com.kindsundev.expense.manager.ui.base

import android.content.Context


interface BaseView {
    fun getCurrentContext(): Context

    fun onLoad()

    fun onError(message: String)

    fun onSuccess()
}
