package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.*
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import io.reactivex.Completable
import io.reactivex.Observable

class TransactionFirebase : BaseFirebase() {

    fun insertTransaction(walletId: Int, transaction: TransactionModel) =
        Completable.create { emitter ->
            initPointerGeneric()
                .child(walletId.toString())
                .child(Constant.MY_REFERENCE_CHILD_TRANSACTION)
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

    fun updateBalance(walletID: Int, income: Double) = Completable.create { emitter ->
        initPointerGeneric().child(walletID.toString())
            .child(Constant.REF_FIELD_BALANCE).setValue(income).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
            }
    }

    fun getTransactions(walletID: String): Observable<TransactionModel> =
        Observable.create { subscriber ->
            initPointerGeneric()
                .child(walletID)
                .child(Constant.MY_REFERENCE_CHILD_TRANSACTION)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
                            for (data in snapshot.children) {
                                val transaction = data.getValue(TransactionModel::class.java)
                                transaction?.let {
                                    subscriber.onNext(it)
                                }
                            }
                            subscriber.onComplete()
                        } else {
                            /*      subscriber.onError(NullPointerException())
                            * if throw exception -> always return onError() and show message
                            * i want to transfer data so i will fake data and check it
                            * */
                            val transaction = TransactionModel()
                            subscriber.onNext(transaction)
                            subscriber.onComplete()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        subscriber.onError(error.toException())
                    }
                })
        }
}