package com.kindsundev.expense.manager.ui.home.bag.adapter

import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel

class BillAdapterPresenter(
    private val view: BillAdapterContract.View
): BillAdapterContract.Presenter {

    override fun handlerCalculateBalanceOfDay(bills: BillModel): Double {
        val result = bills.calculateBalanceForDay()
        return if (result == 0.0) {
            view.onResultColor(Constant.TRANSACTION_STATE_BALANCE)
            result
        } else if (result > 0) {
            view.onResultColor(Constant.TRANSACTION_STATE_INCOME)
            result
        } else {
            view.onResultColor(Constant.TRANSACTION_STATE_EXPENSE)
            result * (-1)
        }
    }
}