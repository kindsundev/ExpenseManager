package com.kindsundev.expense.manager.ui.home.wallet

import com.kindsundev.expense.manager.ui.base.BaseView

interface BagContract {

    interface Presenter {

    }

    interface View : BaseView


    interface Listener {
        fun onClickTransaction(transaction: String)
    }
}