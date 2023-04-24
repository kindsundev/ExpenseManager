package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BagContract {

    interface Presenter {
        fun handleGetWallets()

        fun handleGetBillsAndSort(wallet: WalletModel)
    }

    interface View : BaseView {
        fun onSuccessWallets(result: ArrayList<WalletModel>)

        fun onSuccessBills(result: ArrayList<BillModel>)
    }

    interface Listener {
        fun onClickWalletItem(wallet: WalletModel)
    }
}