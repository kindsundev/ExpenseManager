package com.kindsundev.expense.manager.ui.home.bag.search

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TransactionSearchPresenter(
    val view : TransactionSearchContract.View
) : TransactionSearchContract.Presenter {
    private val compositeDisposable = CompositeDisposable()

    override fun searchTransactionInDay(walletId: Int, date: String) {
        view.onLoad()
        val mBill = ArrayList<BillModel>()
        val disposable = TransactionFirebase().getTransactionsInDay(walletId, date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    val message = view.getCurrentContext().getString(R.string.something_error)
                    view.onError(message)
                    Logger.error(it.message.toString())
                },
                onComplete = { view.onSuccessBills(mBill) },
                onNext = {mBill.add(it)}
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() = compositeDisposable.clear()
}