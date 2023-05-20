package com.kindsundev.expense.manager.ui.home.bag.search

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionSearchContract {

    interface Presenter {
        fun searchTransactionInDay(walletId: Int, date: String)
    }

    interface View : BaseView {
        fun onSuccessBills(bills: ArrayList<BillModel>)
    }

}