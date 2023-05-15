package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.update

import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface UpdatePlanContract {

    interface Presenter {
        fun handleUpdatePlan(
            name: String,
            estimatedAmount: String,
            startDate: String,
            endDate: String,
            walletId: Int,
            currentPlan: PlanModel
        )
    }

    interface View : BaseView {
        fun showMessageInvalidData(message: String)
    }
}