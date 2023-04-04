package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BagContract {

    interface Presenter {
        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>

        fun handlerGetTransactions(walletId: String)

        fun getBills(): ArrayList<BillModel>

        fun handlerCalculateBalanceOfDay(bills: BillModel): Double

        fun updateTransaction(walletID: Int, transaction: TransactionModel)

        fun handlerUpdateBalance(
            walletId: Int,
            transactionType: String,
            currentBalance: Double,
            beforeMoney:Double,
            afterMoney: Double
        )
    }

    interface ViewParent : BaseView {
        fun onSuccess(status: Boolean)

        fun onSuccess(message: String)
    }


    interface ViewChild {
        fun onResultColor(type: String)
    }


    interface Listener {
        fun onClickTransaction(transaction: TransactionModel)
    }
}