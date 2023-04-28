package com.kindsundev.expense.manager.ui.signup

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.AuthFirebase
import com.kindsundev.expense.manager.utils.checkEmailAndPassword
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignUpPresenter(
    private val view: SignUpContract.View
) : SignUpContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val authFirebase by lazy { AuthFirebase() }
    private lateinit var message: String

    override fun handlerSignUp( email: String, password: String) {
        checkDataFromInput(email, password)
        view.onLoad()
        sendRequestSignUp(email, password)
    }

    private fun checkDataFromInput(email: String, password: String) {
        when (checkEmailAndPassword(email, password)) {
            Status.WRONG_EMAIL_EMPTY -> {
                message = view.getCurrentContext().getString(R.string.email_not_null)
                view.onError(message)
            }
            Status.WRONG_EMAIL_PATTERN -> {
                message = view.getCurrentContext().getString(R.string.email_invalidate)
                view.onError(message)
            }
            Status.WRONG_PASSWORD_EMPTY -> {
                message = view.getCurrentContext().getString(R.string.password_not_null)
                view.onError(message)
            }
            Status.WRONG_PASSWORD_LENGTH -> {
                message = view.getCurrentContext().getString(R.string.password_to_short)
                view.onError(message)
            }
            Status.WRONG_EMAIL_PASSWORD_EMPTY -> {
                message = view.getCurrentContext().getString(R.string.email_password_null)
                view.onError(message)
            }
            else -> {}
        }
    }

    private fun sendRequestSignUp(email: String, password: String) {
        val disposableSignUp = authFirebase.signUp(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                message = view.getCurrentContext().getString(R.string.email_already_used)
                view.onError(message)
                Logger.error("Sign Up: ${it.message}")
            })
        compositeDisposable.add(disposableSignUp)
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}