package com.kindsundev.expense.manager.ui.home.bag.adapter

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel

interface BillAdapterContract {

    interface Presenter {
        fun handlerCalculateBalanceOfDay(bills: BillModel): Double
    }

    interface View {
        fun onResultColor(type: String)
    }

    interface Listener {
        fun onClickTransaction(date: String, transaction: TransactionModel)
    }
}