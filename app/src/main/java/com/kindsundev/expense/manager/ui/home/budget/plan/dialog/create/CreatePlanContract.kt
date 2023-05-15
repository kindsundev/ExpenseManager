package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.create

import android.content.Context
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel

interface CreatePlanContract {
    interface Presenter {
        fun handleDataFromInput(
            name: String,
            amount: String,
            startDate: String,
            endDate: String,
        ): PlanModel
    }

    interface View {
        fun getCurrentContext(): Context

        fun showMessageInvalidData(message: String)
    }

    interface Result {
        fun onSuccessPlan(wallet: WalletModel, plan: PlanModel)
    }
}