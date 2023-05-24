package com.kindsundev.expense.manager.ui.home.note.transaction

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.PlanFirebase
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TransactionPresenter(
    val view: TransactionContract.View
) : TransactionContract.Presenter {
    private val transactionFirebase by lazy { TransactionFirebase() }
    private val compositeDisposable = CompositeDisposable()
    private lateinit var message: String

    fun isDataFromInputValid(
        amount: String,
        walletNameHint: String,
        walletNameText: String
    ): Boolean {
        return when {
            amount.isEmpty() -> {
                message = view.getCurrentContext().getString(R.string.please_enter_amount)
                view.onShowMessage(message)
                false
            }
            walletNameHint == view.getCurrentContext()
                .getString(R.string.wallet) && walletNameText.isEmpty()
            -> {
                message = view.getCurrentContext().getString(R.string.please_select_wallet)
                view.onShowMessage(message)
                false
            }
            else -> true
        }
    }

    override fun createTransaction(walletID: Int, transaction: TransactionModel) {
        view.onLoad()
        val disposable = transactionFirebase.insertTransaction(walletID, transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                message = view.getCurrentContext().getString(R.string.create_transaction_failed)
                view.onError(message)
                Logger.error("Create transaction: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handleUpdateBalanceOfWallet(
        walletID: Int,
        transactionType: String,
        balance: Double,
        amount: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val currentBalance = balance - amount
            updateBalanceOfWallet(walletID, currentBalance)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val currentBalance = balance + amount
            updateBalanceOfWallet(walletID, currentBalance)
        }
    }

    private fun updateBalanceOfWallet(walletID: Int, income: Double) {
        val disposable = transactionFirebase.updateBalanceOfWallet(walletID, income)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.create_transaction_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.create_transaction_failed)
                view.onError(message)
                Logger.error("Update balance: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handleUpdateBalanceOfPlan(
        walletId: Int,
        dateKey: String,
        planId: Int,
        transactionType: String,
        balance: Double,
        amount: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val currentBalance = balance - amount
            updateBalanceOfPlan(walletId, dateKey, planId, currentBalance)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val currentBalance = balance + amount
            updateBalanceOfPlan(walletId, dateKey, planId, currentBalance)
        }
    }

    private fun updateBalanceOfPlan(walletId: Int, dateKey: String, planId: Int, balance: Double) {
        view.onLoad()
        val disposable = PlanFirebase().updateBalance(walletId, dateKey, planId, balance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccessPlan()
            }, {
                view.onError(
                    view.getCurrentContext().getString(R.string.something_error)
                )
                Logger.error(it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() = compositeDisposable.dispose()
}