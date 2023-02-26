package com.kindsundev.expense.manager.ui.base


interface BaseView {

    fun onLoad()

    fun onError(message: String)

    fun onSuccess()
}
