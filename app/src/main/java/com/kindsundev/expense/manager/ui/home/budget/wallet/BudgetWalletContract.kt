package com.kindsundev.expense.manager.ui.home.budget.wallet

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetWalletContract {

    interface Presenter {
        fun handleGetWallets()

        fun handleCreateWallet(wallet: WalletModel)
    }

    interface View : BaseView {
        fun onSuccess(message: String)

        fun onSuccessWallets(wallets: ArrayList<WalletModel>)
    }

    interface Listener {
        fun onClickEditWallet(wallet: WalletModel)
    }

    interface Result {
        fun onResultCreateWallet(status: Boolean)
    }
}