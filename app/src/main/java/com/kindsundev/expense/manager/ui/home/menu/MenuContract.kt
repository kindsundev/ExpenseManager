package com.kindsundev.expense.manager.ui.home.menu

import com.google.firebase.auth.FirebaseUser
import com.kindsundev.expense.manager.ui.base.BaseView

interface MenuContract {

    interface Presenter {
        fun getCurrentUser() : FirebaseUser?
    }

    interface View : BaseView {

    }
}