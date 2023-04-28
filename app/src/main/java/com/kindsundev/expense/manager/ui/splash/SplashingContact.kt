package com.kindsundev.expense.manager.ui.splash


interface SplashingContact {

    interface Presenter {
        fun checkLoggedIn()
    }

    interface View {
        fun isLoggedIn()

        fun notLoggedIn()
    }
}