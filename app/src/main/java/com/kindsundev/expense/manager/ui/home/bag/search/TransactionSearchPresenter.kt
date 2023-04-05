package com.kindsundev.expense.manager.ui.home.bag.search

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
    private var mBill = ArrayList<BillModel>()

    override fun searchTransactionInDay(walletID: String, date: String) {
        view.onLoad()
        mBill.clear()
        val disposable = TransactionFirebase().getTransactionsInDay(walletID, date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = { view.onSuccess() },
                onNext = {mBill.add(it)}
            )
        compositeDisposable.add(disposable)
    }

    override fun getBill(): ArrayList<BillModel> = mBill

    fun cleanUp() {
        compositeDisposable.clear()
    }
}