package com.kindsundev.expense.manager.ui.home.note.transaction

import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionContract {

    interface Presenter {
        fun createTransaction(walletID: Int, transaction: TransactionModel)

        fun handleUpdateBalanceOfWallet(
            walletID: Int,
            transactionType: String,
            balance: Double,
            amount: Double
        )

        fun handleUpdateBalanceOfPlan(
            walletId: Int,
            dateKey: String,
            planId: Int,
            transactionType: String,
            balance: Double,
            amount: Double
        )
    }

    interface View : BaseView {
        fun onShowMessage(message: String)

        fun onSuccess(message: String)

        fun onSuccessPlan()
    }
}