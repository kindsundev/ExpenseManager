package com.kindsundev.expense.manager.data.base

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import io.reactivex.Observable

open class BaseFirebase {
    private val mDatabase by lazy { Firebase.database }
    private val myRef by lazy { Constant.MY_REFERENCE_NAME }
    private val mChildUserUID by lazy { UserFirebase().getUserUID() }
    private val mChildWallets by lazy { Constant.MY_REFERENCE_CHILD_WALLETS }

    protected fun initPointerGeneric(): DatabaseReference {
        return mDatabase.getReference(myRef)
            .child(mChildUserUID)
            .child(mChildWallets)
    }

    fun getWallets(): Observable<WalletModel> = Observable.create { subscriber ->
        initPointerGeneric().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
                    for (data in snapshot.children) {
                        val wallet = data.getValue(WalletModel::class.java)
                        wallet?.let {
                            subscriber.onNext(it)
                        }
                    }
                    subscriber.onComplete()
                } else {
                    subscriber.onComplete()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                subscriber.onError(error.toException())
            }
        })
    }
}