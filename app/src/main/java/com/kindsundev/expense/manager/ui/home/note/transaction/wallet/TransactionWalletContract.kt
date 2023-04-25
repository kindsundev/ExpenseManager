package com.kindsundev.expense.manager.ui.home.note.transaction.wallet

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionWalletContract {

    interface Presenter {
        fun handleGetWallets()
    }

    interface View : BaseView {
        fun onSuccessWallets(wallets: ArrayList<WalletModel>)
    }

    interface Listener {
        fun onClickWalletItem(wallet: WalletModel)
    }
}