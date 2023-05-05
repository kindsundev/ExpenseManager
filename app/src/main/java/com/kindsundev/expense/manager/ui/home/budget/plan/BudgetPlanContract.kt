package com.kindsundev.expense.manager.ui.home.budget.plan

import com.kindsundev.expense.manager.data.model.PlanModel
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

        fun onSuccessPlan(plans: ArrayList<PlanModel>)
    }

    interface Listener {
        fun onClickPlanItem(plan: PlanModel)
    }
}