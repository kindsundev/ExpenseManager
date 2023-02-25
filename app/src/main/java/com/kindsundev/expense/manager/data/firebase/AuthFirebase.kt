package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable

class AuthFirebase {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(it.exception!!)
                }
            }
    }

    fun signUp(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
    }

    fun logout() = firebaseAuth.signOut()
}