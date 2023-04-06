package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class BagPresenter(
    private val view: BagContract.View,
) : BagContract.Presenter {
    private val transactionFirebase by lazy { TransactionFirebase() }
    private val compositeDisposable = CompositeDisposable()
    private var mWallets = ArrayList<WalletModel>()
    private var mBills = ArrayList<BillModel>()

    override fun handlerGetWallets() {
        view.onLoad()
        mWallets.clear()
        val disposable = transactionFirebase.getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = { view.onSuccess() },
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    override fun getWallets(): ArrayList<WalletModel> = mWallets

    override fun handlerGetTransactions(walletId: String) {
        mBills.clear()
        val disposable = TransactionFirebase().getTransactions(walletId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = { view.onSuccess(true) },
                onNext = {
                    mBills.add(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun getBills(): ArrayList<BillModel>  = mBills

    fun cleanUp() {
        compositeDisposable.clear()
    }
}