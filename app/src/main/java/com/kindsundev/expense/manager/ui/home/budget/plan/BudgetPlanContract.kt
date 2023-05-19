package com.kindsundev.expense.manager.ui.home.budget.plan

import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetPlanContract {

    interface Presenter {
        fun handleGetPlansInWallet(wallet: WalletModel)

        fun handleCreatePlan(walletId: Int, plan: PlanModel)

        fun handleGetPlansInFirebase(walletId: Int)
    }

    interface View : BaseView {
        fun onSuccess(message: String)

        fun onSuccessPlanMap(plans: ArrayList<PlannedModel>)
    }

    interface Listener {
        fun onClickPlanItem(planned: PlannedModel)
    }
}