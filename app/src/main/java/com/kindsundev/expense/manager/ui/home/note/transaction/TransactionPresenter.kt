package com.kindsundev.expense.manager.ui.home.note.transaction

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TransactionPresenter(
    val view: TransactionContract.View
): TransactionContract.Presenter {
    private val transactionFirebase by lazy { TransactionFirebase() }
    private val compositeDisposable = CompositeDisposable()

    override fun createTransaction(walletID: Int, transaction: TransactionModel) {
        view.onLoad()
        val disposable = transactionFirebase.insertTransaction(walletID, transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Create transaction success")
            }, {
                view.onError("Something went wrong, please try again later!")
                Logger.error("Create transaction: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}