package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.ui.base.BaseView

interface BagContract {

    interface Presenter {


    }

    interface View : BaseView {
        fun onSuccess(key: String)
    }


    interface Listener {
        fun onClickTransaction(transaction: String)
    }
}