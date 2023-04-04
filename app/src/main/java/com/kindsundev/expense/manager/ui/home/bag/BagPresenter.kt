package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class BagPresenter(
    private val bagView: BagContract.ViewParent? = null,
    private val adapterView: BagContract.ViewChild? = null
) : BagContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private var mWallets = ArrayList<WalletModel>()
    private var mBills = ArrayList<BillModel>()

    override fun handlerGetWallets() {
        bagView?.onLoad()
        mWallets.clear()
        val disposable = WalletFirebase().getWallets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { bagView?.onError(it.message.toString()) },
                onComplete = { bagView?.onSuccess() },
                onNext = { mWallets.add(it) }
            )
        compositeDisposable.add(disposable)
    }

    override fun getWallets(): ArrayList<WalletModel> = mWallets

    override fun handlerGetTransactions(walletId: String) {
        mBills.clear()
        val disposable = TransactionFirebase().getTransactions(walletId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { bagView?.onError(it.message.toString()) },
                onComplete = { bagView?.onSuccess(true) },
                onNext = {
                    mBills.add(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun getBills(): ArrayList<BillModel>  = mBills

    override fun handlerCalculateBalanceOfDay(bills: BillModel): Double {
        val result = bills.calculateBalance()
        return if (result == 0.0) {
            adapterView?.onResultColor(Constant.TRANSACTION_STATE_BALANCE)
            result
        } else if (result > 0) {
            adapterView?.onResultColor(Constant.TRANSACTION_STATE_INCOME)
            result
        } else {
            adapterView?.onResultColor(Constant.TRANSACTION_STATE_EXPENSE)
            result * (-1)
        }
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}