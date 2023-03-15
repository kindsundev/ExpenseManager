package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import com.kindsundev.expense.manager.data.model.WalletModel

interface WalletListener {

    fun onClickWalletItem(wallet: WalletModel)

}