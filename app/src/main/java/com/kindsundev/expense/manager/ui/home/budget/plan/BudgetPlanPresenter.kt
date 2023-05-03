package com.kindsundev.expense.manager.ui.home.budget.plan

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class BudgetPlanPresenter(
    private val view: BudgetPlanContract.View
): BudgetPlanContract.Presenter {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val compositeDisposable = CompositeDisposable()
    private val planFirebase = PlanFirebase()
    private lateinit var message: String

    override fun handleGetPlans(wallet: WalletModel) {
        scope.launch {
            val plans = wallet.getPlanList()
            withContext(Dispatchers.Main) {
                view.onSuccessPlan(plans)
            }
        }
    }

    override fun handleCreatePlan(walletId: Int, plan: PlanModel) {
        view.onLoad()
        val disposable = planFirebase.upsertPlan(walletId, plan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.create_plan_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.create_plan_failed)
                view.onError(message)
                Logger.error("Create plan: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() = compositeDisposable.dispose()
}