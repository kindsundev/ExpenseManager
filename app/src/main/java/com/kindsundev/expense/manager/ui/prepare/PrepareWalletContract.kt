package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface  PrepareWalletContract {

    interface Presenter {
        fun handlerCreateWallet(wallet: WalletModel)

        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>
    }

    interface View: BaseView {}

    interface Listener {
        fun onClickWalletItem(walletModel: WalletModel)
    }

    interface Result {
        fun onResultCreateWallet (status: Boolean)
    }
}