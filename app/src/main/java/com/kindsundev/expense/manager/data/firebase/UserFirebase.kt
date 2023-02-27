package com.kindsundev.expense.manager.data.firebase


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kindsundev.expense.manager.common.Logger

class UserFirebase {
    private var _user = Firebase.auth.currentUser
    val user get() = _user

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        Logger.info("First get current user: ${user?.email}")
        FirebaseAuth.AuthStateListener { listener ->
            _user?.let {
                _user = listener.currentUser as FirebaseUser
            }
        }
        Logger.info("Last get current user: ${user?.email}")
    }

}