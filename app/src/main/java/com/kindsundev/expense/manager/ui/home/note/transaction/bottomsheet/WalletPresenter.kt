package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WalletPresenter(
    private val view: WalletContract.View
) : WalletContract.Presenter {
    private val walletFirebase by lazy { WalletFirebase() }
    private val compositeDisposable = CompositeDisposable()

    override fun handlerCreateWallet(id: Int, name: String, currency: String, balance: String) {
        view.onLoad()
        val wallet = WalletModel(id, name, currency, balance.toDouble())
        val disposable = walletFirebase.insertWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Create success")
            }, {
                view.onError("Please check data from input")
                Logger.error("Sign In: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}
