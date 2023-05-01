package com.kindsundev.expense.manager.ui.home.budget.plan.wallet

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetWalletContract {
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