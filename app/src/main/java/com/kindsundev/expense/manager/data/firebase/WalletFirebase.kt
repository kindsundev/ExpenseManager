package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.Completable
import io.reactivex.Observable

class WalletFirebase : BaseFirebase() {

    fun upsertWallet(wallet: WalletModel) = Completable.create { emitter ->
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

    fun getWallet(walletId: Int) : Observable<WalletModel> = Observable.create { subscriber ->
        initPointerGeneric().child(walletId.toString())
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ((!subscriber.isDisposed)) {
                        val wallet = snapshot.getValue(WalletModel::class.java)
                        wallet?.let {
                            subscriber.onNext(it)
                            subscriber.onComplete()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    subscriber.onError(error.toException())
                }
            })
    }

    fun deleteWallet(wallet: WalletModel) = Completable.create { emitter ->
        initPointerGeneric().child(wallet.id.toString()).removeValue()
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