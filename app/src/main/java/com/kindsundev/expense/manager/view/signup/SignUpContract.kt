package com.kindsundev.expense.manager.view.signup

class SignUpContract {

    interface PresenterInterface {
        fun handlerSignUp(email: String, password: String)
    }

    interface ViewInterface {
        fun onLoading()

        fun onError(message: String)

        fun onSuccess()
    }
}