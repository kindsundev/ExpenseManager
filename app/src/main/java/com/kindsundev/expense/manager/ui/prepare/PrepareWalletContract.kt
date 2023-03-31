package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface  PrepareWalletContract {

    interface Presenter {

        fun handlerGetWallets()

        fun getWallets() : ArrayList<WalletModel>

        fun handlerGetTransactions(walletId: Int)

        fun getBills(): ArrayList<BillModel>

    }

    interface View: BaseView {

        fun onSuccess(status: Boolean)

    }

    interface Listener {

        fun onClickWalletItem(walletModel: WalletModel)

        fun resultCreateWallet(status: Boolean)

    }
}