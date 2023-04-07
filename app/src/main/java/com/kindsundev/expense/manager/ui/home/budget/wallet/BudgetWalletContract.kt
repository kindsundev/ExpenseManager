package com.kindsundev.expense.manager.ui.home.budget.wallet

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetWalletContract {

    interface Presenter {
        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>
    }

    interface View : BaseView

    interface Listener {

        fun onClickEditWallet(wallet: WalletModel)

        fun onClickDeleteWallet(walletId: String)
    }
}