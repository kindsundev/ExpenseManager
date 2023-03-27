package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.Completable
import io.reactivex.Observable

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

}