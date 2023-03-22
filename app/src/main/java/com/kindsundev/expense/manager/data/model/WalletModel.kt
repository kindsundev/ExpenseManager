package com.kindsundev.expense.manager.data.model

/*
* Why do i set default values here?
* => Firstly, when creating a wallet, it can have only it but not transactions
* => Second, i avoid bug when using realtime database
* */

data class WalletModel (
    val id: Int? = 0,
    val name: String? = "",
    val currency: String? = "",
    val origin: Double? = 0.0,
    val balance: Double? = 0.0,
    val transaction: TransactionModel? = null
)