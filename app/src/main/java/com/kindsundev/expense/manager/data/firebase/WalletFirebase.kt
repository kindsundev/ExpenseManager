package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.WalletContract
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.Flow.Subscriber

class WalletFirebase : BaseFirebase() {

    fun insertWallet(wallet: WalletModel) = Completable.create { emitter ->
        initPointerGeneric().child(wallet.id.toString()).setValue(wallet)
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

    fun getWallets(): Observable<WalletModel> = Observable.create { subscriber ->
        initPointerGeneric().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    for (data in snapshot.children) {
                        val wallet = data.getValue(WalletModel::class.java)
                        wallet?.let {
                            subscriber.onNext(it)
                        }
                    }
                    subscriber.onComplete()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                subscriber.onError(error.toException())
            }
        })
    }
}