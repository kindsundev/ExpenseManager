package com.kindsundev.expense.manager.view.signin

class SignInContract {

    interface PresenterInterface {
        fun handlerSignIn(email: String, password: String)
    }

    interface ViewInterface {
        fun onLoading()

        fun onError(message: String)

        fun onSuccess()
    }
}