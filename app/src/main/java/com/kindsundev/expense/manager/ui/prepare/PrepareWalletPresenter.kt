package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PrepareWalletPresenter(
    val view: PrepareWalletContract.View
) : PrepareWalletContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private var mWallets = ArrayList<WalletModel>()
    private var mBill = ArrayList<BillModel>()

    override fun handlerGetWallets() {
        view.onLoad()
        mWallets.clear()
        val disposable = BaseFirebase().getWallets()
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

    override fun handlerGetTransactions(walletId: Int) {
        view.onLoad()
        mBill.clear()
        val disposable = TransactionFirebase().getTransactions(walletId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = { view.onSuccess(true) },
                onNext = {
                    mBill.add(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun getBills(): ArrayList<BillModel>  = mBill

    fun cleanUp() {
        compositeDisposable.clear()
    }

}