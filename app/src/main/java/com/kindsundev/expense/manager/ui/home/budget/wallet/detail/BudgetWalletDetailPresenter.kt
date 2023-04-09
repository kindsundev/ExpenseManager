package com.kindsundev.expense.manager.ui.home.budget.wallet.detail

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BudgetWalletDetailPresenter(
    private val view: BudgetWalletDetailContract.View
) : BudgetWalletDetailContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val walletFirebase by lazy { WalletFirebase() }

    override fun updateWallet(wallet: WalletModel) {
        view.onLoad()
        val disposable = walletFirebase.upsertWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Update Wallet Success")
            }, {
                view.onError("Something error, please try again later!")
                Logger.error("Update Wallet: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun deleteWallet(wallet: WalletModel) {
        view.onLoad()
        val disposable = walletFirebase.deleteWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Delete Wallet Success")
            }, {
                view.onError("Something error, please try again later!")
                Logger.error("Delete Wallet: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}