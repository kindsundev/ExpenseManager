package com.kindsundev.expense.manager.base

interface BaseView {

    fun onLoad()

    fun onError(message: String)

    fun onSuccess()
}