package com.kindsundev.expense.manager.ui.home.menu.profile

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.data.model.UserModel
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

    override fun updateEmail(email: String) {
        checkValidEmail(email)
        view.onLoad()
        val disposable = user.updateEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess("Email update success")
            }, {
                view.onError("You need to login again before updating")
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
                view.onError("You need to login again before updating")
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

    override fun getUserAfterUpdate(): UserModel {
        val userAuth =  UserFirebase().user
        val id = userAuth?.uid
        val photoUri = userAuth?.photoUrl.toString()
        val name = userAuth?.displayName
        val email = userAuth?.email
        val phoneNumber = userAuth?.phoneNumber
        return UserModel(id, photoUri, name, email, phoneNumber)
    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }
}