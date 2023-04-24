package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BagPresenter(
    private val view: BagContract.View,
) : BagContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val walletFirebase = WalletFirebase()

    override fun handleGetWallets() {
        view.onLoad()
        val wallets = ArrayList<WalletModel>()
        val disposable = walletFirebase.getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { view.onError(it.message.toString()) },
                onComplete = { view.onSuccessWallets(wallets) },
                onNext = { wallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    override fun handleGetBillsAndSort(wallet: WalletModel) {
        scope.launch {
            val bills = wallet.getBills()
            val result = sortBillsByNewest(bills)
            withContext(Dispatchers.Main) {
                view.onSuccessBills(ArrayList(result))
            }
        }
    }

    private fun sortBillsByNewest(data: ArrayList<BillModel>): List<BillModel> {
        var list : List<BillModel> = listOf()
        if (data.isNotEmpty()) {
            list = data.sortedByDescending { bill ->
                bill.date?.let {
                    SimpleDateFormat("dd E MMMM yyyy", Locale.ENGLISH).parse(it)
                }
            }
        }
        return list
    }

    fun cleanUp() = compositeDisposable.clear()
}