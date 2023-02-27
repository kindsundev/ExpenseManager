package com.kindsundev.expense.manager.ui.home.menu.profile

import android.net.Uri
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfilePresenter(
    private val view: ProfileContact.View
) : ProfileContact.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val user = UserFirebase()

    override fun updateProfile(uri: Uri?, name: String) {
        if (name.isEmpty()) {
            view.onError("Please enter your name")
        }
        view.onLoad()
        sendRequestUpdateProfile(uri, name)
    }

    private fun sendRequestUpdateProfile(uri: Uri?, name: String) {
        val disposable = user.updateProfile(uri, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess()
            }, {
                view.onError("Update failed")
                Logger.error("Update profile: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    override fun updatePassword(password: String) {

    }

    fun cleanUp() {
        compositeDisposable.dispose()
    }

}