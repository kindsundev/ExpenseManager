package com.kindsundev.expense.manager.ui.home.report.wallet

import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface ReportWalletContract {

    interface Presenter {
        fun handlerCreateWallet(wallet: WalletModel)

        fun handlerGetWallets()
    }

    interface View : BaseView {
        fun onSuccess(message: String)
    }

    interface Listener {
        fun onClickWalletItem(wallet: WalletModel)
    }

}