package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface  PrepareWalletContract {

    interface Presenter {
        fun handleCreateWallet(wallet: WalletModel)

        fun handleGetWallets()
    }

    interface View: BaseView {
        fun onSuccessWallets(wallets: ArrayList<WalletModel>)
    }

    interface Listener {
        fun onClickWalletItem(walletModel: WalletModel)
    }

    interface Result {
        fun onResultCreateWallet (status: Boolean)
    }
}