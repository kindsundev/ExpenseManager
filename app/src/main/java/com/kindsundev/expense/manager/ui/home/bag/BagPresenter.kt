package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class BagPresenter(
    private val view: BagContract.View,
) : BagContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun handleGetWallets() {
        view.onLoad()
        val wallets = ArrayList<WalletModel>()
        val disposable = WalletFirebase().getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    val message = view.getCurrentContext().getString(R.string.something_error)
                    view.onError(message)
                    Logger.error(it.message.toString())
                },
                onComplete = { view.onSuccessWallets(wallets) },
                onNext = { wallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    override fun handleGetBillsAndSort(wallet: WalletModel) {
        scope.launch {
            val bills = wallet.getBills()
            val result = wallet.sortBillsByNewest(bills)
            withContext(Dispatchers.Main) {
                view.onSuccessBills(ArrayList(result))
            }
        }
    }

    fun cleanUp() = compositeDisposable.clear()
}