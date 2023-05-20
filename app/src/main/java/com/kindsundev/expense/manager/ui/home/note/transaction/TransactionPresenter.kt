package com.kindsundev.expense.manager.ui.home.note.transaction

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
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

    override fun handlerUpdateBalance(
        walletID: Int,
        transactionType: String,
        balance: Double,
        amount: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val currentBalance = balance - amount
            updateBalance(walletID, currentBalance)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val currentBalance = balance + amount
            updateBalance(walletID, currentBalance)
        }
    }

    private fun updateBalance(walletID: Int, income: Double) {
        val disposable = transactionFirebase.updateBalance(walletID, income)
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
    
    fun cleanUp() {
        compositeDisposable.dispose()
    }
}