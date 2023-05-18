package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.update

import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface UpdatePlanContract {

    interface Presenter {
        fun handleDataFromInput(
            name: String,
            estimatedAmount: String,
            startDate: String,
            endDate: String,
            walletId: Int,
            currentPlan: PlanModel
        )

        fun handleUpdatePlan(walletId: Int, dateKey: String, plan: PlanModel)
    }

    interface View : BaseView {
        fun showMessageInvalidData(message: String)

        fun onPlanValidation(plan: PlanModel)

        fun onSuccess(walletId: Int, dateKey: String, planId: Int)
    }

    interface Listener {
        fun requestUpdateData(walletId: Int, dateKey: String, planId: Int)
    }
}