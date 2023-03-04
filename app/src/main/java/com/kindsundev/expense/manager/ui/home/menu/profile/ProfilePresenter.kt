package com.kindsundev.expense.manager.ui.home.menu.profile

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.utils.checkEmail
import com.kindsundev.expense.manager.utils.checkName
import com.kindsundev.expense.manager.utils.checkPassword
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfilePresenter(
    private val view: ProfileContact.View
) : ProfileContact.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val user = UserFirebase()

    override fun securityRequired(oldToken: String, newToken: String): Boolean {
        if (oldToken.compareTo(newToken) == 0) {
            return true
        }
        return false
    }
    
    override fun updateAvatar(uri: String?) {
        view.onLoad()
        val disposable = user.updateAvatar(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Avatar update success")
            }, {
                view.onError("Avatar update failed")
                Logger.error("Avatar update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    override fun updateName(name: String) {
        checkValidName(name)
        view.onLoad()
        val disposable = user.updateName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Name update success")
            }, {
                view.onError("Name update failed")
                Logger.error("Name update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    private fun checkValidName(name: String){
        when(checkName(name)) {
            Status.WRONG_NAME_EMPTY -> view.onError("Don't name to empty")
            Status.WRONG_NAME_SHORT -> view.onError("Don't name to short!")
            Status.WRONG_NAME_LONG -> view.onError("Don't name to long!")
            Status.WRONG_NAME_HAS_DIGITS -> view.onError("Name cannot have digits!")
            Status.WRONG_NAME_HAS_SPECIAL_CHARACTER ->
                view.onError("Name cannot have special character!")
            else -> {}
        }
    }

    override fun updateEmail(email: String) {
        checkValidEmail(email)
        view.onLoad()
        val disposable = user.updateEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Email update success")
            }, {
                view.onError("Email update failed")
                Logger.error("Email update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    private fun checkValidEmail(email: String) {
        when (checkEmail(email)) {
            Status.WRONG_EMAIL_EMPTY -> view.onError("Email mus not be null")
            Status.WRONG_EMAIL_PATTERN -> view.onError("Email invalidate")
            else -> {}
        }
    }

    override fun updatePassword(password: String) {
        checkValidPassword(password)
        view.onLoad()
        val disposable = user.updatePassword(password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Password update success")
            }, {
                view.onError("Password update failed")
                Logger.error("Password update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    private fun checkValidPassword(password: String) {
        when (checkPassword(password)) {
            Status.WRONG_PASSWORD_EMPTY -> view.onError("Password must be not null")
            Status.WRONG_PASSWORD_LENGTH -> view.onError("Password must be greater than or equal to 6")
            else -> {}
        }
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}