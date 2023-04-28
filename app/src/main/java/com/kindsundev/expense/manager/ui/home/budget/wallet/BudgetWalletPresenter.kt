package com.kindsundev.expense.manager.ui.home.budget.wallet

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
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
    private lateinit var message : String

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
                    Logger.error(it.message.toString())
                },
                onComplete = { view.onSuccessWallets(mWallets) },
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    override fun handleCreateWallet(wallet: WalletModel) {
        view.onLoad()
        val disposable = walletFirebase.upsertWallet(wallet)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.create_wallet_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.check_data_from_input)
                view.onError(message)
                Logger.error("Create wallet: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() : Unit = compositeDisposable.clear()
}