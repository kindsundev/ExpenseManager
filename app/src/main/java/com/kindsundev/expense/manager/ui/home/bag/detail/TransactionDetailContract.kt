package com.kindsundev.expense.manager.ui.home.bag.detail

import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionDetailContract {

    interface Presenter {

        fun updateTransaction(walletId: Int, dateKey: String, transaction: TransactionModel)

        fun handleUpdateBalanceOfWallet(
            walletId: Int,
            transactionType: String,
            currentBalance: Double,
            beforeMoney: Double,
            afterMoney: Double
        )

        fun handleDeleteTransaction(walletId: Int, date: String, transactionId: Int)

        fun checkAndRestoreBalance(
            walletId: Int,
            transactionType: String,
            currentBalance: Double,
            beforeMoney: Double,
            ownPlan: Boolean,
            dateKey: String? = null,
            planId: Int? = null,
        )

        fun handleExtractionPlan(wallet: WalletModel, planId: Int?)

        fun handleUpdateBalanceOfPlan(
            walletId: Int,
            dateKey: String,
            planId: Int,
            transactionType: String,
            currentBalance: Double,
            beforeMoney: Double,
            afterMoney: Double?
        )
    }

    interface View : BaseView {
        fun onExtractionPlanSuccess(planned: PlannedModel)

        fun onUpdateTransactionSuccess(message: String)

        fun onUpdateBalanceWalletSuccess(isDeleteAction: Boolean)

        fun onUpdateBalancePlanSuccess()

        fun onDeleteTransactionSuccess(message: String)
    }

    interface Result {
        fun onActionSuccess()
    }
}