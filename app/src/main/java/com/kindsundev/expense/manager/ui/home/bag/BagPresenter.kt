package com.kindsundev.expense.manager.ui.home.bag

import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.TransactionFirebase
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


    /*
    * I will check each type of transaction
    * And compare previous amount vs current amount
    * Then calculate and compensate for a reasonable wallet
    * */
    override fun handlerUpdateBalance(
        walletId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney:Double,
        afterMoney: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            compareAndCalculateNewExpense(walletId, currentBalance, beforeMoney, afterMoney)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            compareAndCalculateNewIncome(walletId, currentBalance, beforeMoney, afterMoney)
        }
    }


    /*
    *   EXPENSE EXAMPLE DEMO:    [walletCurrentBalance] = 100, [previousMoney] = 20
    * ---------------------------------------------------------------------------
    * case 1: [previousMoney] > [currentMoney]      // (currentMoney = 10)
    * case 2: [previousMoney] < [currentMoney]      // (currentMoney = 40)
    *
    * We have [previousBalance] = 20 => [originalBalance] = 120 (*)
    *    - C1: if update 20 to 10 -> plus 10 => [walletBalance] = 110
    *    - C2: if update 20 to 40 -> minus 20 => [walletBalance] = 80
    * */
    private fun compareAndCalculateNewExpense(
        walletId: Int,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double
    ){
        if (beforeMoney > afterMoney) {
            val surplus = beforeMoney - afterMoney
            val income = currentBalance + surplus
            updateBalance(walletId, income)
        } else {
            val surplus = afterMoney - beforeMoney
            val expense = currentBalance - surplus
            updateBalance(walletId, expense)
        }
    }


    /*
    *   INCOME EXAMPLE DEMO:    [walletCurrentBalance] = 120, [previousMoney] = 20
    * ---------------------------------------------------------------------------
    * case 1: [previousMoney] > [currentMoney]      // (currentMoney = 10)
    * case 2: [previousMoney] < [currentMoney]      // (currentMoney = 40)
    *
    * We have [previousBalance] = 20 => [originalBalance] = 100 (*)
    *    - C1: if update 20 to 10 -> minus 10 => [walletBalance] = 90
    *    - C2: if update 20 to 40 -> plus 20 => [walletBalance] = 120
    * */
    private fun compareAndCalculateNewIncome(
        walletId: Int,
        currentBalance: Double,
        beforeMoney: Double,
        afterMoney: Double
    ) {
        if (beforeMoney > afterMoney) {
            val surplus = beforeMoney - afterMoney
            val expense = currentBalance - surplus
            updateBalance(walletId, expense)
        } else {
            val surplus = afterMoney - beforeMoney
            val income = currentBalance + surplus
            updateBalance(walletId, income)
        }
    }

    private fun updateBalance(walletID: Int, newValue: Double) {
        val disposable = transactionFirebase.updateBalance(walletID, newValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bagView?.onSuccess("Update transaction success")
            }, {
                bagView?.onError(it.message!!)
                Logger.error("Update balance: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun handlerDeleteTransaction(walletID: Int, date: String, transactionId: Int) {
        bagView?.onLoad()
        val disposable = transactionFirebase
            .deleteTransaction(walletID.toString(), date, transactionId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bagView?.onSuccess(true)
            }, {
                bagView?.onError(it.message!!)
                Logger.error("Delete transaction: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    override fun checkAndRestoreBalance(
        walletId: Int,
        transactionType: String,
        currentBalance: Double,
        beforeMoney: Double
    ) {
        if (transactionType == Constant.TRANSACTION_TYPE_EXPENSE) {
            val income = currentBalance + beforeMoney
            updateBalance(walletId, income)
        } else if (transactionType == Constant.TRANSACTION_TYPE_INCOME) {
            val expense = currentBalance - beforeMoney
            updateBalance(walletId, expense)
        }
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}