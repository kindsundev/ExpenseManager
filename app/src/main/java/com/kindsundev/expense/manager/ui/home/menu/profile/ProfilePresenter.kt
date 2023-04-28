package com.kindsundev.expense.manager.ui.home.menu.profile

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/*
* Why i always ask to re-login when user request to update some sensitive fields.
* Because using firebase service i don't have permission to get old password for authentication.
* Or maybe the user who logged in elsewhere has changed their email or password...
* so in the current version of the login, the update will fail.
* */

class ProfilePresenter(
    private val view: ProfileContact.View
) : ProfileContact.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val user = UserFirebase()
    private lateinit var message: String
    
    override fun updateAvatar(uri: String?) {
        view.onLoad()
        val disposable = user.updateAvatar(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.update_avatar_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.update_avatar_failed)
                view.onError(message)
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
                message = view.getCurrentContext().getString(R.string.update_name_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.update_name_failed)
                view.onError(message)
                Logger.error("Name update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    override fun updateEmail(email: String) {
        view.onLoad()
        val disposable = user.updateEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.update_email_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.request_re_login)
                view.onError(message)
                Logger.error("Email update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    override fun updatePassword(password: String) {
        view.onLoad()
        val disposable = user.updatePassword(password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message = view.getCurrentContext().getString(R.string.update_password_success)
                view.onSuccess(message)
            }, {
                message = view.getCurrentContext().getString(R.string.request_re_login)
                view.onError(message)
                Logger.error("Password update failed: ${it.message}")
            })
        compositeDisposable.add(disposable)
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