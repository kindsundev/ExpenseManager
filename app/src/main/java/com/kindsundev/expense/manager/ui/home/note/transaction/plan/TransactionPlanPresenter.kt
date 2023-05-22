package com.kindsundev.expense.manager.ui.home.note.transaction.plan

import com.kindsundev.expense.manager.data.model.WalletModel
import kotlinx.coroutines.*

class TransactionPlanPresenter(
    private val view: TransactionPlanContract.View
) : TransactionPlanContract.Presenter {

    override fun handleGetPlans(wallet: WalletModel) {
        view.onLoad()
        CoroutineScope(Dispatchers.Default + SupervisorJob())
            .launch {
                val result = wallet.getPlannedList()
                withContext(Dispatchers.Main) {
                    view.onSuccessPlans(result)
                }
            }
    }

}