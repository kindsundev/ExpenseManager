package com.kindsundev.expense.manager.ui.home.note.transaction

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
    private var transactionType: String? = null

    override fun createTransaction(walletID: Int, transaction: TransactionModel) {
        transactionType = transaction.type
        view.onLoad()
        val disposable = transactionFirebase.insertTransaction(walletID, transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                view.onError("Something went wrong, please try again later!")
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
                view.onSuccess("Create transaction success")
            }, {
                view.onError(it.message!!)
                Logger.error("Update balance: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }
    
    fun cleanUp() {
        compositeDisposable.dispose()
    }
}