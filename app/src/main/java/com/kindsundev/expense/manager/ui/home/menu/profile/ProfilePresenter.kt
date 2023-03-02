package com.kindsundev.expense.manager.ui.home.menu.profile

import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.utils.checkUpdateUserDataNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfilePresenter(
    private val view: ProfileContact.View
) : ProfileContact.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val user = UserFirebase()

    override fun updateProfile(uri: String?, name: String, email: String, password: String) {
        view.onLoad()
        checkDataFromInput(name, email, password)
        sendRequestUpdateAvatarAndName(uri, name)


    }

    private fun checkDataFromInput(name: String, email: String, password: String) {
        when (checkUpdateUserDataNull(name, email, password)) {
            Status.WRONG_DATA_NULL -> view.onError("Please fill in data")
            else -> {}
        }
    }

    private fun sendRequestUpdateAvatarAndName(uri: String?, name: String) {
        val disposable = user.updateAvatarAndName(uri, name)
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

    fun cleanUp() {
        compositeDisposable.dispose()
    }

}