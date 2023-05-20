package com.kindsundev.expense.manager.ui.home.bag.detail

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TransactionDetailPresenter(
    private val view: TransactionDetailContract.View
): TransactionDetailContract.Presenter {
    private val transactionFirebase by lazy { TransactionFirebase() }
    private val compositeDisposable = CompositeDisposable()
    private lateinit var message: String
    private var actionDelete = false

    override fun updateTransaction(walletID: Int, transaction: TransactionModel) {
        view.onLoad()
        val disposable = TransactionFirebase().upsertTransaction(walletID, transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                message = view.getCurrentContext().getString(R.string.something_error)
                view.onError(message)
                Logger.error("Create transaction: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }


    /*
    * I will check each type of transaction
    * And compare previous amount vs current amount
    * Then calculate and compensate for a reasonable wallet
    * */
    override fun handlerUpdateBalance(
        walletId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney:Double,
        afterMoney: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            compareAndCalculateNewExpense(walletId, currentBalance, beforeMoney, afterMoney)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            compareAndCalculateNewIncome(walletId, currentBalance, beforeMoney, afterMoney)
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
    private fun compareAndCalculateNewExpense(
        walletId: Int,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double
    ){
        if (beforeMoney > afterMoney) {
            val surplus = beforeMoney - afterMoney
            val income = currentBalance + surplus
            updateBalance(walletId, income)
        } else {
            val surplus = afterMoney - beforeMoney
            val expense = currentBalance - surplus
            updateBalance(walletId, expense)
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
    private fun compareAndCalculateNewIncome(
        walletId: Int,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double
    ) {
        if (beforeMoney > afterMoney) {
            val surplus = beforeMoney - afterMoney
            val expense = currentBalance - surplus
            updateBalance(walletId, expense)
        } else {
            val surplus = afterMoney - beforeMoney
            val income = currentBalance + surplus
            updateBalance(walletId, income)
        }
    }

    private fun updateBalance(walletID: Int, newValue: Double) {
        val disposable = transactionFirebase.updateBalance(walletID, newValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.update_transaction_success)
                if (actionDelete) {
                    message = view.getCurrentContext().getString(R.string.delete_transaction_success)
                }
                view.onSuccess(message)
                actionDelete = false
            }, {
                message = view.getCurrentContext().getString(R.string.update_balance_failed)
                view.onError(message)
                Logger.error("Update balance: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handlerDeleteTransaction(walletID: Int, date: String, transactionId: Int) {
        view.onLoad()
        val disposable = transactionFirebase
            .deleteTransaction(walletID, date, transactionId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                actionDelete = true
                view.onSuccess(true)
            }, {
                message = view.getCurrentContext().getString(R.string.delete_transaction_failed)
                view.onError(message)
                Logger.error("Delete transaction: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun checkAndRestoreBalance(
        walletId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val income = currentBalance + beforeMoney
            updateBalance(walletId, income)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val expense = currentBalance - beforeMoney
            updateBalance(walletId, expense)
        }
    }

    fun cleanUp() = compositeDisposable.clear()
}