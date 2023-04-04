package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface  PrepareWalletContract {

    interface Presenter {

        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>

    }

    interface View: BaseView {}

    interface Listener {

        fun onClickWalletItem(walletModel: WalletModel)

        fun resultCreateWallet(status: Boolean)

    }
}