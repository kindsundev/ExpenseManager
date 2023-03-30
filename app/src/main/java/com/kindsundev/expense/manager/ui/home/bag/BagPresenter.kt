package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.model.TransactionModel

class BagPresenter(
    val view: BagContract.View
) : BagContract.Presenter {

    fun sameTimeFilter(transactions: ArrayList<TransactionModel>): ArrayList<TransactionModel> {

        return ArrayList()
    }
}