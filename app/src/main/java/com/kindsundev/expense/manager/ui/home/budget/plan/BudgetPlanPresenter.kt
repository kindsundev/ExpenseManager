package com.kindsundev.expense.manager.ui.home.budget.plan

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.utils.getCurrentDateTime
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class BudgetPlanPresenter(
    private val view: BudgetPlanContract.View
) : BudgetPlanContract.Presenter {
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
            val plans = wallet.getPlannedList()
            withContext(Dispatchers.Main) {
                view.onSuccessPlans(plans)
            }
        }
    }

    override fun handleCreatePlan(walletId: Int, plan: PlanModel) {
        view.onLoad()
        val disposable = planFirebase.insertPlan(walletId, plan)
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
        val list = ArrayList<PlannedModel>()
        val disposable = planFirebase.getPlanMap(walletId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    message = view.getCurrentContext().getString(R.string.something_error)
                    view.onError(message)
                    Logger.warn(it.message.toString())
                },
                onComplete = { view.onSuccessPlans(list) },
                onNext = { list.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    override fun handleExtractionStatusOfPlan(plans: ArrayList<PlannedModel>): ArrayList<PlanModel> {
        val planList = ArrayList<PlanModel>()
        plans.forEach { planned ->
            val plan = planned.plan!!.copy()
            if ((plan.currentBalance!! >= plan.estimatedAmount!!) && (!planList.contains(plan))) {
                plan.isDone = true
                planList.add(plan)
            }
            if (isNearDueDate(plan.endDate!!) && (!planList.contains(plan))) {
                plan.isNearDueDate = true
                planList.add(plan)
            }
        }
        return planList
    }

    private fun isNearDueDate(endDate: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.getDefault())
        val current = sdf.parse(getCurrentDateTime())
        val end = sdf.parse(endDate)
        val differenceInMillis = (end!!.time) - (current!!.time)
        val differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
        return differenceInDays in 1L..3L
    }

    fun cleanUp() = compositeDisposable.dispose()
}