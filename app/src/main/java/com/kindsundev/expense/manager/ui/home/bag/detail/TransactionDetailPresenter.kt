package com.kindsundev.expense.manager.ui.home.bag.detail

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class TransactionDetailPresenter(
    private val view: TransactionDetailContract.View
) : TransactionDetailContract.Presenter {
    private val transactionFirebase by lazy { TransactionFirebase() }
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val compositeDisposable = CompositeDisposable()

    override fun handleExtractionPlan(wallet: WalletModel, planId: Int?) {
        scope.launch {
            val planned = wallet.getPlanned(planId)
            withContext(Dispatchers.Main) {
                view.onExtractionPlanSuccess(planned)
            }
        }
    }

    override fun updateTransaction(walletId: Int, dateKey: String, transaction: TransactionModel) {
        view.onLoad()
        val disposable = TransactionFirebase().updateTransaction(walletId, dateKey, transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onUpdateTransactionSuccess(
                    view.getCurrentContext().getString(R.string.update_transaction_success)
                )
            }, {
                view.onError(view.getCurrentContext().getString(R.string.something_error))
                Logger.error(it.message!!)
            })
        compositeDisposable.add(disposable)
    }

    /*
    * I will check each type of transaction
    * And compare previous amount vs current amount
    * Then calculate and compensate for a reasonable wallet
    * */
    override fun handleUpdateBalanceOfWallet(
        walletId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            calculateNewExpenseBalance(
                walletId, currentBalance, beforeMoney, afterMoney, false, null, null)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            calculateNewIncomeBalance(
                walletId, currentBalance, beforeMoney, afterMoney, false, null, null)
        }
    }

    /*
    *   EXPENSE EXAMPLE DEMO:    [walletCurrentBalance] = 100, [previousMoney] = 20
    * ---------------------------------------------------------------------------
    * case 1: [previousMoney] > [currentMoney]      // (currentMoney = 10)
    * case 2: [previousMoney] < [currentMoney]      // (currentMoney = 40)
    *
    * We have [previousBalance] = 20 => [originalBalance] = 120 (*)
    *    - C1: if update 20 to 10 -> plus 10 => [walletBalance] = 110
    *    - C2: if update 20 to 40 -> minus 20 => [walletBalance] = 80
    * */
    private fun calculateNewExpenseBalance(
        walletId: Int,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double,
        ownPlan: Boolean,
        planId: Int?,
        dateKey: String?
    ) {
        if (beforeMoney > afterMoney) {
            val surplus = beforeMoney - afterMoney
            val income = currentBalance + surplus
            if (ownPlan) {
                updateBalanceOfPlan(walletId, dateKey!!, planId!!, income)
            } else {
                updateBalanceOfWallet(walletId, income, false)
            }
        } else {
            val surplus = afterMoney - beforeMoney
            val expense = currentBalance - surplus
            if (ownPlan) {
                updateBalanceOfPlan(walletId, dateKey!!, planId!!, expense)
            } else {
                updateBalanceOfWallet(walletId, expense, false)
            }
        }
    }

    /*
    *   INCOME EXAMPLE DEMO:    [walletCurrentBalance] = 120, [previousMoney] = 20
    * ---------------------------------------------------------------------------
    * case 1: [previousMoney] > [currentMoney]      // (currentMoney = 10)
    * case 2: [previousMoney] < [currentMoney]      // (currentMoney = 40)
    *
    * We have [previousBalance] = 20 => [originalBalance] = 100 (*)
    *    - C1: if update 20 to 10 -> minus 10 => [walletBalance] = 90
    *    - C2: if update 20 to 40 -> plus 20 => [walletBalance] = 120
    * */
    private fun calculateNewIncomeBalance(
        walletId: Int,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double,
        ownPlan: Boolean,
        planId: Int?,
        datePlanKey: String?
    ) {
        if (beforeMoney > afterMoney) {
            val surplus = beforeMoney - afterMoney
            val expense = currentBalance - surplus
            if (ownPlan) {
                updateBalanceOfPlan(walletId, datePlanKey!!, planId!!, expense)
            } else {
                updateBalanceOfWallet(walletId, expense, false)
            }
        } else {
            val surplus = afterMoney - beforeMoney
            val income = currentBalance + surplus
            if (ownPlan) {
                updateBalanceOfPlan(walletId, datePlanKey!!, planId!!, income)
            } else {
                updateBalanceOfWallet(walletId, income, false)
            }
        }
    }

    private fun updateBalanceOfWallet(walletID: Int, newValue: Double, isDeleteAction: Boolean) {
        view.onLoad()
        val disposable = transactionFirebase.updateBalanceOfWallet(walletID, newValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onUpdateBalanceWalletSuccess(isDeleteAction)
            }, {
                view.onError(view.getCurrentContext().getString(R.string.update_balance_failed))
                Logger.error(it.message!!)
            })
        compositeDisposable.add(disposable)
    }

    override fun handleUpdateBalanceOfPlan(
        walletId: Int,
        dateKey: String,
        planId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double?
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            calculateNewExpenseBalance(
                walletId, currentBalance, beforeMoney, afterMoney!!, true, planId, dateKey)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            calculateNewIncomeBalance(
                walletId, currentBalance, beforeMoney, afterMoney!!, true, planId, dateKey)
        }
    }

    private fun updateBalanceOfPlan(
        walletId: Int,
        datePlanKey: String,
        planId: Int,
        balance: Double
    ) {
        view.onLoad()
        val disposable = PlanFirebase().updateBalance(walletId, datePlanKey, planId, balance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onUpdateBalancePlanSuccess()
            }, {
                view.onError(view.getCurrentContext().getString(R.string.something_error))
                Logger.error(it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    override fun handleDeleteTransaction(walletId: Int, date: String, transactionId: Int) {
        view.onLoad()
        val disposable = transactionFirebase.deleteTransaction(walletId, date, transactionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onDeleteTransactionSuccess(
                    view.getCurrentContext().getString(R.string.delete_transaction_success)
                )
            }, {
                view.onError(view.getCurrentContext().getString(R.string.delete_transaction_failed))
                Logger.error(it.message!!)
            })
        compositeDisposable.add(disposable)
    }

    override fun checkAndRestoreBalance(
        walletId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney: Double,
        ownPlan: Boolean,
        dateKey: String?,
        planId: Int?
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val income = currentBalance + beforeMoney
            if (ownPlan) {
                updateBalanceOfPlan(walletId, dateKey!!, planId!!, income)
            } else {
                updateBalanceOfWallet(walletId, income, true)
            }
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val expense = currentBalance - beforeMoney
            if (ownPlan) {
                updateBalanceOfPlan(walletId, dateKey!!, planId!!, expense)
            } else {
                updateBalanceOfWallet(walletId, expense, true)
            }
        }
    }

    fun cleanUp() = compositeDisposable.clear()
}