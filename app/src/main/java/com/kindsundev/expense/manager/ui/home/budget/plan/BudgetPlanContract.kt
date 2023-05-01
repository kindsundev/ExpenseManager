package com.kindsundev.expense.manager.ui.home.budget.plan

import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetPlanContract {

    interface Presenter {
        fun handleGetPlans()

        fun handleCreatePlan()
    }

    interface View : BaseView {
        fun onSuccessPlan(plans: ArrayList<PlanModel>)
    }
}