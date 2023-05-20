package com.kindsundev.expense.manager.ui.home.note.transaction.plan

import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface TransactionPlanContract {

    interface Presenter {
        fun handleGetPlans(wallet: WalletModel)
    }

    interface View : BaseView {
        fun onSuccessPlans(plans: ArrayList<PlanModel>)
    }

    interface Listener {
        fun onClickPlanItem(plan: PlanModel)
    }
}