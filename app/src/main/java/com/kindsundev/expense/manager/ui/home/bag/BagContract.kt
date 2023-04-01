package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BagContract {

    interface Presenter {

    }

    interface ViewParent : BaseView {}


    interface ViewChild {
        fun onResultColor(type: String)
    }


    interface Listener {
        fun onClickTransaction(transaction: TransactionModel)
    }
}