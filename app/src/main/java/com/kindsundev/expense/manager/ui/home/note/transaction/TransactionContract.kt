package com.kindsundev.expense.manager.ui.home.note.transaction

import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionContract {

    interface Presenter {
        fun createTransaction(walletID: Int, transaction: TransactionModel)

        fun handlerUpdateBalance(walletID: Int, transactionType: String, balance: Double, amount: Double)
    }

    interface View : BaseView {
        fun onSuccess(message: String)
    }
}