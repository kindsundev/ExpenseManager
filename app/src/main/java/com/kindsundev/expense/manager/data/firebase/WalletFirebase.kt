package com.kindsundev.expense.manager.data.firebase

import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.Completable

class WalletFirebase: BaseFirebase() {

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