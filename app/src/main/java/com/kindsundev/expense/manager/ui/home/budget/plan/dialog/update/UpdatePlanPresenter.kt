package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.update

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UpdatePlanPresenter(
    private val view: UpdatePlanContract.View
) : UpdatePlanContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun handleDataFromInput(
        name: String,
        estimatedAmount: String,
        startDate: String,
        endDate: String,
        walletId: Int,
        currentPlan: PlanModel
    ) {
        if (name == currentPlan.name
            && estimatedAmount.toDouble() == currentPlan.estimatedAmount
            && startDate == currentPlan.startDate
            && endDate == currentPlan.endDate) {
            view.showMessageInvalidData(
                view.getCurrentContext().getString(R.string.no_data_changed)
            )
        } else {
            if (isValidData(name, startDate, endDate, estimatedAmount)) {
                currentPlan.name = name
                currentPlan.startDate = startDate
                currentPlan.endDate = endDate
                val amount = estimatedAmount.toDouble()
                if (amount != currentPlan.estimatedAmount) currentPlan.estimatedAmount = amount
                view.onPlanValidation(currentPlan)
            }
        }
    }

    override fun handleUpdatePlan(walletId: Int, dateKey: String, plan: PlanModel) {
        view.onLoad()
        val disposable = PlanFirebase().updatePlan(walletId, dateKey, plan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess(walletId, dateKey, plan.id!!)
            }, {
                view.onError(
                    view.getCurrentContext().getString(R.string.update_plan_failed)
                )
            })
        compositeDisposable.add(disposable)
    }

    private fun isValidData(
        name: String,
        startDate: String,
        endDate: String,
        estimatedAmount: String,
    ): Boolean {
        return if (name.isNotEmpty() && estimatedAmount.isNotEmpty()
            && startDate != view.getCurrentContext().getString(R.string.start_day)
            && endDate != view.getCurrentContext().getString(R.string.end_day)
        ) {
            val amountToLarge = estimatedAmount.replace(",", "").length > 15
            return when {
                amountToLarge -> {
                    view.showMessageInvalidData(
                        view.getCurrentContext().getString(R.string.amount_to_large)
                    )
                    false
                }
                !isValidDate(startDate, endDate) -> {
                    view.showMessageInvalidData(
                        view.getCurrentContext().getString(R.string.please_provide_valid_date_plan)
                    )
                    false
                }
                else -> true
            }
        } else {
            view.showMessageInvalidData(
                view.getCurrentContext().getString(R.string.please_provide_full_data)
            )
            false
        }
    }

    private fun isValidDate(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val differenceInMillis = (end!!.time) - (start!!.time)
        val differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
        return differenceInDays > 0
    }

    fun cleanUp(): Unit = compositeDisposable.dispose()
}