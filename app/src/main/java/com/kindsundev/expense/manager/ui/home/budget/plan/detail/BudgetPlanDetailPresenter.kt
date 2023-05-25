package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class BudgetPlanDetailPresenter(
    private val view: BudgetPlanDetailContract.View
) : BudgetPlanDetailContract.Presenter {
    private val planFirebase by lazy { PlanFirebase() }
    private val compositeDisposable = CompositeDisposable()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun handleDeletePlan(walletId: Int, dateKey: String, planId: Int) {
        view.onLoad()
        val disposable = planFirebase.deletePlan(walletId, dateKey, planId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                view.onError(view.getCurrentContext().getString(R.string.delete_plan_failed))
            })
        compositeDisposable.add(disposable)
    }

    override fun handleGetPlan(walletId: Int, dateKey: String, planId: Int) {
        view.onLoad()
        var plan = PlanModel()
        val disposable = planFirebase.getPlan(walletId, dateKey, planId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    view.onError(
                        view.getCurrentContext().getString(R.string.something_error)
                    )
                    Logger.error(it.message.toString())
                },
                onComplete = { view.onSuccessPlan(plan) },
                onNext = { plan = it }
            )
        compositeDisposable.add(disposable)
    }

    override fun handleExtractionBills(wallet: WalletModel, planId: Int) {
        view.onLoad()
        scope.launch {
            val bills = wallet.getBillsOfPlan(planId)
            val result = wallet.sortBillsByNewest(bills)
            withContext(Dispatchers.Main) {
                view.onSuccessBill(ArrayList(result), false)
            }
        }
    }

    override fun handleGetBills(walletId: Int, planId: Int, isRequestPlan: Boolean) {
        view.onLoad()
        val bills = ArrayList<BillModel>()
        val disposable = WalletFirebase().getWallet(walletId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    view.onError(
                        view.getCurrentContext().getString(R.string.something_error)
                    )
                },
                onComplete = { view.onSuccessBill(bills, isRequestPlan) },
                onNext = {
                    bills.addAll(ArrayList(it.sortBillsByNewest(it.getBillsOfPlan(planId))))
                }
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() = compositeDisposable.dispose()
}