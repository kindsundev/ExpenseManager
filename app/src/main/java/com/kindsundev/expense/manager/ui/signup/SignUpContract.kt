package com.kindsundev.expense.manager.ui.signup

import com.kindsundev.expense.manager.ui.base.BaseView

interface SignUpContract {

    interface Presenter {
        fun handlerSignUp(email: String, password: String)
    }

    interface View : BaseView
}