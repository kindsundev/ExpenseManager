package com.kindsundev.expense.manager.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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

    fun updateAvatarAndName(uri: String?, name: String) = Completable.create() { emitter ->
        val profileUpdates = initProfileUpdates(uri, name)
        if (profileUpdates == null) {
            emitter.onError(NullPointerException())
        } else {
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

    private fun initProfileUpdates(uri: String?, name: String): UserProfileChangeRequest? {
        var profileUpdates: UserProfileChangeRequest? = null
        if (name.isEmpty()) {
            profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(uri)
            }
        } else if (uri == null) {
            profileUpdates = userProfileChangeRequest {
                displayName = name
            }
        } else if (name.isNotEmpty() and uri.isNotEmpty()) {
            profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = Uri.parse(uri)
            }
        }
        return profileUpdates
    }

}