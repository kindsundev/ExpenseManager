package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
import com.kindsundev.expense.manager.data.firebase.WalletFirebase
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class BagPresenter(
    private val bagView: BagContract.ViewParent? = null,
    private val adapterView: BagContract.ViewChild? = null
) : BagContract.Presenter {
    private val transactionFirebase by lazy { TransactionFirebase() }
    private val compositeDisposable = CompositeDisposable()
    private var mWallets = ArrayList<WalletModel>()
    private var mBills = ArrayList<BillModel>()

    override fun handlerGetWallets() {
        bagView?.onLoad()
        mWallets.clear()
        val disposable = transactionFirebase.getWallets()
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

    override fun updateTransaction(walletID: Int, transaction: TransactionModel) {
        bagView?.onLoad()
        val disposable = TransactionFirebase().upsertTransaction(walletID, transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bagView?.onSuccess()
            }, {
                bagView?.onError("Something went wrong, please try again later!")
                Logger.error("Create transaction: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handlerUpdateBalance(
        walletID: Int,
        transactionType: String,
        balance: Double,
        amount: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val currentBalance = balance - amount
            updateBalance(walletID, currentBalance)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val currentBalance = balance + amount
            updateBalance(walletID, currentBalance)
        }
    }

    private fun updateBalance(walletID: Int, income: Double) {
        val disposable = transactionFirebase.updateBalance(walletID, income)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bagView?.onSuccess("Create transaction success")
            }, {
                bagView?.onError(it.message!!)
                Logger.error("Update balance: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}