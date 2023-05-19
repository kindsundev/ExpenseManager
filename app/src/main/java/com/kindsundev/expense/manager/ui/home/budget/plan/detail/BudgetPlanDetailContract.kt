package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetPlanDetailContract {

    interface Presenter {
        fun handleDeletePlan(walletId: Int, dateKey: String, planId: Int)

        fun handleGetPlan(walletId: Int, dateKey: String, planId: Int)
    }

    interface View : BaseView {
        fun onSuccess(message: String)

        fun onSuccessPlan(plan: PlanModel)
    }

}