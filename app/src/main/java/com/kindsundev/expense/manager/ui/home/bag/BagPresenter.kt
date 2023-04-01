package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.BillModel

class BagPresenter(
    val view: BagContract.ViewParent? = null,
    val viewChild: BagContract.ViewChild? = null
) : BagContract.Presenter {

    fun handlerCalculateBalance(bills: BillModel): Double {
        val result = bills.calculateBalance()
        return if (result == 0.0) {
            viewChild?.onResultColor(Constant.TRANSACTION_STATE_BALANCE)
            result
        } else if (result > 0) {
            viewChild?.onResultColor(Constant.TRANSACTION_STATE_INCOME)
            result
        } else {
            viewChild?.onResultColor(Constant.TRANSACTION_STATE_EXPENSE)
            result * (-1)
        }
    }
}