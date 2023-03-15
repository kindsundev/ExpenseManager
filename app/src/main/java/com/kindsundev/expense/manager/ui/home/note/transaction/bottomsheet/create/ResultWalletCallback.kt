package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.create

import com.kindsundev.expense.manager.data.model.WalletModel

interface ResultWalletCallback {

    fun onCreateWalletResult(wallet: WalletModel)
}