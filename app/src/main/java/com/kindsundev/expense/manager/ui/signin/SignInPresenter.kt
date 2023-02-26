package com.kindsundev.expense.manager.ui.signin

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.AuthFirebase
import com.kindsundev.expense.manager.utils.checkEmailAndPassword
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignInPresenter(
    private var view: SignInContract.View
) : SignInContract.Presenter {

    private val authFirebase by lazy { AuthFirebase() }
    private val compositeDisposable = CompositeDisposable()

    override fun handlerSignIn(email: String, password: String) {
        checkDataFromInput(email, password)
        view.onLoad()
        sendRequestSignIn(email, password)
    }

    private fun checkDataFromInput(email: String, password: String) {
        when (checkEmailAndPassword(email, password)) {
            Status.WRONG_EMAIL_EMPTY -> {
                view.onError("Email mus not be null")
            }
            Status.WRONG_EMAIL_PATTERN -> {
                view.onError("Email invalidate")
            }
            Status.WRONG_PASSWORD_EMPTY -> {
                view.onError("Password must not be null")
            }
            Status.WRONG_PASSWORD_LENGTH -> {
                view.onError("Password must be greater than or equal to 6")
            }
            Status.WRONG_EMAIL_PASSWORD_EMPTY -> {
                view.onError("Please input full email and password")
            }
            else -> {}
        }
    }

    private fun sendRequestSignIn(email: String, password: String) {
        val disposable = authFirebase.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                view.onError("Login failed")
                Logger.error("Sign In: ${it.message!!}")
            })
        compositeDisposable.add(disposable)
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}