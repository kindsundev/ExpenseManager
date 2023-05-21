package com.kindsundev.expense.manager.ui.home.bag.detail

import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionDetailContract {

    interface Presenter {

        fun updateTransaction(walletId: Int, dateKey: String, transaction: TransactionModel)

        fun handlerUpdateBalance(
            walletId: Int,
            transactionType: String,
            currentBalance: Double,
            beforeMoney:Double,
            afterMoney: Double
        )

        fun handlerDeleteTransaction(walletId: Int, date: String, transactionId: Int)

        fun checkAndRestoreBalance(
            walletId: Int,
            transactionType: String,
            currentBalance: Double,
            beforeMoney: Double
        )
    }

    interface View : BaseView {
        fun onSuccess(status: Boolean)

        fun onSuccess(message: String)
    }

    interface Result {
        fun onActionSuccess()
    }
}