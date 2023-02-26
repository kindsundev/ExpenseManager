package com.kindsundev.expense.manager.ui.splash

import com.kindsundev.expense.manager.data.firebase.AuthFirebase
import com.kindsundev.expense.manager.data.firebase.UserFirebase

class SplashingPresenter(
    private val view: SplashingContact.View
) : SplashingContact.Presenter {

    override fun checkLoggedIn() {
        val user = UserFirebase().user
        if (user == null) {
            view.notLoggedIn()
        } else{
            view.isLoggedIn()
        }
    }

}