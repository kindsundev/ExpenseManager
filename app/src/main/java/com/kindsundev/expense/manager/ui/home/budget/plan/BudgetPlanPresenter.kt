package com.kindsundev.expense.manager.ui.home.budget.plan

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class BudgetPlanPresenter(
    private val view: BudgetPlanContract.View
): BudgetPlanContract.Presenter {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val compositeDisposable = CompositeDisposable()
    private val planFirebase = PlanFirebase()
    private lateinit var message: String

    /*
    * when the user selects the wallet,
    * it already has the data of the plans,
    * so instead of calling firebase we will extract the data
    * */
    override fun handleGetPlansInWallet(wallet: WalletModel) {
        view.onLoad()
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

    // call to firebase when plan created (update ui)
    override fun handleGetPlansInFirebase(walletId: Int) {
        view.onLoad()
        val plans = ArrayList<PlanModel>()
        val disposable = planFirebase.getPlanList(walletId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    message = view.getCurrentContext().getString(R.string.something_error)
                    view.onError(message)
                    Logger.warn(it.message.toString())
                },
                onComplete = { view.onSuccessPlan(plans) },
                onNext = { plans.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() = compositeDisposable.dispose()
}