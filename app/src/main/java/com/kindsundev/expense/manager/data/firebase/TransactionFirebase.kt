package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.*
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.utils.getCurrentDate
import com.kindsundev.expense.manager.data.model.BillModel
import io.reactivex.Completable
import io.reactivex.Observable

class TransactionFirebase : BaseFirebase() {

    private fun initPointerTransaction(walletId: Int) =
        initPointerGeneric()
            .child(walletId.toString())
            .child(Constant.MY_REFERENCE_CHILD_TRANSACTIONS)

    fun insertTransaction(walletId: Int, transaction: TransactionModel) =
        Completable.create { emitter ->
            initPointerTransaction(walletId)
                .child(getCurrentDate())
                .child(transaction.id.toString())
                .setValue(transaction)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun updateTransaction(walletId: Int, dateKey: String, transaction: TransactionModel) =
        Completable.create { emitter ->
            initPointerTransaction(walletId)
                .child(dateKey)
                .child(transaction.id.toString())
                .setValue(transaction)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun updateBalanceOfWallet(walletId: Int, newValue: Double) = Completable.create { emitter ->
        initPointerGeneric().child(walletId.toString())
            .child(Constant.REF_FIELD_BALANCE_OF_WALLET).setValue(newValue)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
            }
    }

    fun getTransactionsInDay(walletId: Int, date: String) : Observable<BillModel> =
        Observable.create { subscriber ->
            initPointerTransaction(walletId)
            .child(date)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
                        val transactions = ArrayList<TransactionModel>()
                        for (data in snapshot.children) {
                            val transaction = data.getValue(TransactionModel::class.java)
                            transaction?.let {
                                transactions.add(it)
                            }
                        }
                        val bill = BillModel(date, transactions)
                        subscriber.onNext(bill)
                        subscriber.onComplete()
                    } else {
                        subscriber.onNext(BillModel())
                        subscriber.onComplete()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    subscriber.onError(error.toException())
                }
            })
    }

    fun deleteTransaction(walletId: Int, dateKey: String, transactionId: Int) =
        Completable.create { emitter ->
            initPointerTransaction(walletId)
                .child(dateKey)
                .child(transactionId.toString())
                .removeValue()
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }
}