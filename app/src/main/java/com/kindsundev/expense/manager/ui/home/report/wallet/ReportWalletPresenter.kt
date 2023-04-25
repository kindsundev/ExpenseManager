package com.kindsundev.expense.manager.ui.home.report.wallet

import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ReportWalletPresenter(
    private val view: ReportWalletContract.View
): ReportWalletContract.Presenter {
    private val compositeDisposable = CompositeDisposable()

    override fun handleGetWallets() {
        view.onLoad()
        val mWallets: ArrayList<WalletModel> = ArrayList()
        val disposable = WalletFirebase().getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = {view.onSuccessWallets(mWallets)},
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() : Unit = compositeDisposable.dispose()
}