package com.kindsundev.expense.manager.view.signup

interface SignUpContract {

    interface Presenter {
        fun handlerSignUp(email: String, password: String)
    }

    interface View {
        fun onLoading()

        fun onError(message: String)

        fun onSuccess()
    }
}