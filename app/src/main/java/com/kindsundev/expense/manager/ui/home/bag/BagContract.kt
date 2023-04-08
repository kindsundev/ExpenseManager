package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BagContract {

    interface Presenter {
        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>

        fun handlerGetTransactions(walletId: String)

        fun getBills(): ArrayList<BillModel>

    }

    interface View : BaseView {
        fun onSuccess(status: Boolean)
    }

    interface Listener {
        fun onClickWalletItem(wallet: WalletModel)
    }
}