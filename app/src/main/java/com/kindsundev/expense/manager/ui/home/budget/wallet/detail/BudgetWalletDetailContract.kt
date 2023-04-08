package com.kindsundev.expense.manager.ui.home.budget.wallet.detail

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetWalletDetailContract {

    interface Presenter {
        fun updateWallet(wallet: WalletModel)
    }

    interface View : BaseView

}