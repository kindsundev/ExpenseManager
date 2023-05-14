package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetPlanDetailContract {

    interface Presenter {

    }

    interface View : BaseView {
        fun onSuccess(message: String)
    }

}