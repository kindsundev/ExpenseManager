package com.kindsundev.expense.manager.ui.home.budget.plan.dialog

import android.content.Context
import com.kindsundev.expense.manager.data.model.PlanModel

interface CreatePlanContract {
    interface Presenter {
        fun handleDataFromInput(
            name: String,
            amount: String,
            startDate: String,
            endDate: String,
            wallet: String
        ): PlanModel
    }

    interface View {
        fun getCurrentContext(): Context

        fun showMessageInvalidData(message: String)
    }

    interface Result {
        fun onSuccessPlan(walletId: Int, plan: PlanModel)
    }
}