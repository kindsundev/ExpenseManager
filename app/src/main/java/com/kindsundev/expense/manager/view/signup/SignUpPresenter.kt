package com.kindsundev.expense.manager.view.signup

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.FirebaseAuthentication
import com.kindsundev.expense.manager.utils.emailAndPasswordIsValid
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignUpPresenter(
    private val viewInterface: SignUpContract.ViewInterface
) : SignUpContract.PresenterInterface {

    private val firebaseAuthentication by lazy { FirebaseAuthentication() }
    private val user by lazy { firebaseAuthentication.currentUser() }
    private val disposables = CompositeDisposable()

    override fun handlerSignUp(email: String, password: String) {
        checkDataFromInput(email, password)
        viewInterface.onLoading()
        val disposable = firebaseAuthentication.signup(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewInterface.onSuccess()
            }, {
                viewInterface.onError("Register failed")
                Logger.error("Sign Up: ${it.message!!}")
            })
        disposables.add(disposable)
    }

    private fun checkDataFromInput(email: String, password: String) {
        when(emailAndPasswordIsValid(email, password)) {
            Status.WRONG_EMAIL_EMPTY -> {
                viewInterface.onError("Email mus not be null")
            }
            Status.WRONG_EMAIL_PATTERN -> {
                viewInterface.onError("Email invalidate")
            }
            Status.WRONG_PASSWORD_EMPTY -> {
                viewInterface.onError("Password must not be null")
            }
            Status.WRONG_PASSWORD_LENGTH -> {
                viewInterface.onError("Password must be greater than or equal to 6")
            }
            Status.WRONG_EMAIL_PASSWORD_EMPTY -> {
                viewInterface.onError("Please input full email and password")
            }
            else -> { }
        }
    }

    fun onCleared() {
        disposables.dispose()
    }
}