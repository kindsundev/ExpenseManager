package com.kindsundev.expense.manager.ui.home.menu.profile

import com.kindsundev.expense.manager.ui.base.BaseView

interface ProfileContact {

    interface Presenter {

        fun updateAvatar(uri: String?)

        fun updateName(name: String)

        fun updateEmail(email: String)

        fun updatePassword(password: String)

        fun securityRequired(oldToken: String, newToken: String): Boolean
    }

    interface View : BaseView {

        fun onSuccess(message: String)
    }
}