package com.kindsundev.expense.manager.data.base

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.firebase.UserFirebase

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
}