package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.*
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.utils.getCurrentDate
import com.kindsundev.expense.manager.data.model.BillModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class TransactionFirebase : BaseFirebase() {

    /*
    * If you point at a transaction that doesn't have an id, it will create it,
    * otherwise it will update
    * */
    fun upsertTransaction(walletId: Int, transaction: TransactionModel) =
        Completable.create { emitter ->
            initPointerGeneric()
                .child(walletId.toString())
                .child(Constant.MY_REFERENCE_CHILD_TRANSACTION)
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

    fun updateBalance(walletID: Int, newValue: Double) = Completable.create { emitter ->
        initPointerGeneric().child(walletID.toString())
            .child(Constant.REF_FIELD_BALANCE).setValue(newValue).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
            }
    }

    fun getTransactions(walletID: String): Observable<BillModel> =
        Observable.create { subscriber ->
            initPointerGeneric()
                .child(walletID)
                .child(Constant.MY_REFERENCE_CHILD_TRANSACTION)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        handlerResultWhenGetTransactionOnSuccess(subscriber, snapshot)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        subscriber.onError(error.toException())
                    }
                })
        }

    private fun handlerResultWhenGetTransactionOnSuccess(
        subscriber: ObservableEmitter<BillModel>,
        snapshot: DataSnapshot
    ) {
        if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
            for (data in snapshot.children) {
                val transactions = ArrayList<TransactionModel>()
                val date  = data.key
                for (child in data.children) {
                    val dataChild = child.getValue(TransactionModel::class.java)
                    dataChild?.let { transactions.add(it) }
                }
                val bill = BillModel(date, transactions)
                subscriber.onNext(bill)
            }
            subscriber.onComplete()
        } else {
            //  subscriber.onError(NullPointerException())
            val transaction = BillModel("Null", ArrayList())
            subscriber.onNext(transaction)
            subscriber.onComplete()
        }
    }

    fun getTransactionsInDay(walletID: String, date: String) : Observable<BillModel> =
        Observable.create { subscriber ->
        initPointerGeneric()
            .child(walletID)
            .child(Constant.MY_REFERENCE_CHILD_TRANSACTION)
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
                        val transaction = BillModel("Null", ArrayList())
                        subscriber.onNext(transaction)
                        subscriber.onComplete()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    subscriber.onError(error.toException())
                }
            })
    }

    fun deleteTransaction(walletID: String, dateKey: String, transactionId: String) =
        Completable.create { emitter ->
            initPointerGeneric()
                .child(walletID)
                .child(Constant.MY_REFERENCE_CHILD_TRANSACTION)
                .child(dateKey)
                .child(transactionId)
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