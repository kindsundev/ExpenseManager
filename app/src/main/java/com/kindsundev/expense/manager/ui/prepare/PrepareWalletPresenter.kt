package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PrepareWalletPresenter(
    val view: PrepareWalletContract.View
) : PrepareWalletContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private var walletFirebase = WalletFirebase()

    override fun handleCreateWallet(wallet: WalletModel) {
        view.onLoad()
        val disposable = walletFirebase.upsertWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                view.onError("Please check data from input")
                Logger.error("Create wallet: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handleGetWallets() {
        view.onLoad()
        val mWallets = ArrayList<WalletModel>()
        val disposable = walletFirebase.getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = { view.onSuccessWallets(mWallets) },
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }

}