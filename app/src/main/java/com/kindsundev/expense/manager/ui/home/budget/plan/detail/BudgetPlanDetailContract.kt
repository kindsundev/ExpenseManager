package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface BudgetPlanDetailContract {

    interface Presenter {
        fun handleDeletePlan(walletId: Int, dateKey: String, planId: Int)

        fun handleGetPlan(walletId: Int, dateKey: String, planId: Int)

        fun handleExtractionBills(wallet: WalletModel, planId: Int)

        fun handleGetBills(walletId: Int, planId: Int, isRequestPlan: Boolean = false)
    }

    interface View : BaseView {
        fun onSuccess(message: String)

        fun onSuccessPlan(plan: PlanModel)

        fun onSuccessBill(bills: ArrayList<BillModel>, isRequestPlan: Boolean)
    }

}