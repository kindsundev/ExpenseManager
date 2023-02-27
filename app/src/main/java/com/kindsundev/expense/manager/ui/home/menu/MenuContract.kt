package com.kindsundev.expense.manager.ui.home.menu

import com.google.firebase.auth.FirebaseUser
import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface MenuContract {

    interface Presenter {
        fun getCurrentUser() : UserModel
    }

    interface View : BaseView {

    }
}