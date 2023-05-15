package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.utils.dateFormatConversion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BudgetPlanDetailPresenter(
    private val view: BudgetPlanDetailContract.View
) {
    private val planFirebase by lazy { PlanFirebase() }
    private val compositeDisposable = CompositeDisposable()

    fun handleDeletePlan(walletId: Int, startDate: String, planId: Int) {
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

    fun cleanUp() = compositeDisposable.dispose()

}