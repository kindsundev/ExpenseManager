package com.kindsundev.expense.manager.ui.home.report.wallet

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ReportWalletPresenter(
    private val view: ReportWalletContract.View
): ReportWalletContract.Presenter {
    private val walletFirebase by lazy { WalletFirebase() }
    private val compositeDisposable = CompositeDisposable()

    private var mWallets: ArrayList<WalletModel> = ArrayList()

    override fun handlerCreateWallet(wallet: WalletModel) {
        view.onLoad()
        val disposable = walletFirebase.upsertWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Create success")
            }, {
                view.onError("Please check data from input")
                Logger.error("Create wallet: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handlerGetWallets() {
        view.onLoad()
        val disposable = walletFirebase.getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = {view.onSuccess()},
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun getWallets(): ArrayList<WalletModel> {
        return mWallets
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}