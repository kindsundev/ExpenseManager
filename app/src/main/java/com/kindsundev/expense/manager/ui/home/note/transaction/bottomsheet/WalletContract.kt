package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface WalletContract {

    interface Presenter {
        fun handlerCreateWallet(id: Int, name: String, currency: String, balance: String)

        fun handlerGetWallets()
    }

    interface View : BaseView {
        fun onSuccess(message: String)
    }

    interface Listener {
        fun onClickWalletItem(wallet: WalletModel)
    }
}