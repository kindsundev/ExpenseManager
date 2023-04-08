package com.kindsundev.expense.manager.ui.home.budget.wallet.detail

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetWalletDetailContract {

    interface Presenter {
        fun updateWallet(wallet: WalletModel)

        fun deleteWallet(wallet: WalletModel)
    }

    interface View : BaseView {
        fun onSuccess(message: String)
    }

    interface Result {
        fun onSuccessAndRequiredRefreshData(status: Boolean)
    }

}