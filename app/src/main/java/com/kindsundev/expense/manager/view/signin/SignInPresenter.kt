package com.kindsundev.expense.manager.view.signin

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.FirebaseAuthentication
import com.kindsundev.expense.manager.utils.emailAndPasswordIsValid
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignInPresenter(
    private var viewInterface: SignInContract.ViewInterface
) : SignInContract.PresenterInterface {

    private val firebaseAuthentication by lazy { FirebaseAuthentication() }
    private val user by lazy { firebaseAuthentication.currentUser() }
    private val disposables = CompositeDisposable()

    override fun handlerSignIn(email: String, password: String) {
        checkDataFromInput(email, password)
        viewInterface.onLoading()
        val disposable = firebaseAuthentication.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewInterface.onSuccess()
                Logger.info("User name: ${user!!.displayName}, email verified: ${user!!.isEmailVerified}")
            }, {
                viewInterface.onError("Login failed")
                Logger.error(it.message!!)
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