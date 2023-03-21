package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.dialog


interface ResultWalletCallback {

    interface Create {
        fun resultCreateWallet (status: Boolean)
    }

    interface Update {
        fun resultUpdateWallet (status: Boolean)
    }

    interface Remove{
        fun resultRemoveWallet (status: Boolean)
    }


}