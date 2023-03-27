package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface  PrepareWalletContract {

    interface Presenter {

        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>

        fun handlerGetTransactions(walletId: Int)

        fun getTransactions(): ArrayList<TransactionModel>

    }

    interface View: BaseView {

        fun onSuccess(status: Boolean)

    }

    interface Listener {

        fun onClickWalletItem(walletModel: WalletModel)

        fun resultCreateWallet(status: Boolean)

    }
}