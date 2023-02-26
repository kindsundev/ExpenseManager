package com.kindsundev.expense.manager.ui.home.menu

import com.google.firebase.auth.FirebaseUser
import com.kindsundev.expense.manager.data.firebase.UserFirebase

class MenuPresenter(
    private val  view: MenuContract.View
) : MenuContract.Presenter {

    override fun getCurrentUser(): FirebaseUser? {
        return UserFirebase().user
    }
}