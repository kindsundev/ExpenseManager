package com.kindsundev.expense.manager.ui.prepare

import com.kindsundev.expense.manager.R
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
    private lateinit var message: String

    override fun handleCreateWallet(wallet: WalletModel) {
        view.onLoad()
        val disposable = walletFirebase.upsertWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                message = view.getCurrentContext().getString(R.string.check_data_from_input)
                view.onError(message)
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
                onError = {
                    message = view.getCurrentContext().getString(R.string.something_error)
                    view.onError(message)
                    Logger.error("Get wallets: ${it.message}")
                },
                onComplete = { view.onSuccessWallets(mWallets) },
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }

}