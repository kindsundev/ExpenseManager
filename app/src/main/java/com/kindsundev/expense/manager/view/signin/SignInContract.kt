package com.kindsundev.expense.manager.view.signin

interface SignInContract {

    interface Presenter {
        fun handlerSignIn(email: String, password: String)
    }

    interface View {
        fun onLoad()

        fun onError(message: String)

        fun onSuccess()
    }
}