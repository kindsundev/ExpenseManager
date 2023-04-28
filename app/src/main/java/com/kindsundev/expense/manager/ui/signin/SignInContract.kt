package com.kindsundev.expense.manager.ui.signin

import com.kindsundev.expense.manager.ui.base.BaseView

interface SignInContract {

    interface Presenter {
        fun handlerSignIn(email: String, password: String)
    }

    interface View : BaseView
}