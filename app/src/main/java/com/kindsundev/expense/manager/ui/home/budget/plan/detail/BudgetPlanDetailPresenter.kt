package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.utils.dateFormatConversion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class BudgetPlanDetailPresenter(
    private val view: BudgetPlanDetailContract.View
): BudgetPlanDetailContract.Presenter {
    private val planFirebase by lazy { PlanFirebase() }
    private val compositeDisposable = CompositeDisposable()

    override fun handleDeletePlan(walletId: Int, startDate: String, planId: Int) {
        val dateKey = dateFormatConversion(startDate)
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

    override fun handleGetPlan(walletId: Int, startDate: String, planId: Int) {
        view.onLoad()
        var plan = PlanModel()
        val disposable = planFirebase.getPlan(walletId, startDate, planId)
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
                onNext = {plan = it}
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() = compositeDisposable.dispose()

}