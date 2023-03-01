package com.kindsundev.expense.manager.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable

class UserFirebase {
    private var _user = Firebase.auth.currentUser
    val user get() = _user

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        FirebaseAuth.AuthStateListener { listener ->
            _user?.let {
                _user = listener.currentUser as FirebaseUser
            }
        }
    }

    fun updateProfile(uri: String?, name: String) = Completable.create() { emitter ->
        val profileUpdates = userProfileChangeRequest  {
            displayName = name
            photoUri = Uri.parse(uri)
        }
        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
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