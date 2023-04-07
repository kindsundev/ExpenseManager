package com.kindsundev.expense.manager.ui.home.budget.wallet

import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class BudgetWalletPresenter(
    private val view: BudgetWalletContract.View
) : BudgetWalletContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val walletFirebase by lazy { WalletFirebase() }
    private var mWallets = ArrayList<WalletModel>()

    override fun handlerGetWallets() {
        view.onLoad()
        mWallets.clear()
        val disposable = walletFirebase.getWallets()
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

    fun cleanUp() {
        compositeDisposable.clear()
    }
}