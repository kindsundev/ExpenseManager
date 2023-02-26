package com.kindsundev.expense.manager.view.home.menu

import com.kindsundev.expense.manager.base.BaseView

interface MenuContract {

    interface Presenter {
        fun loadCurrentUserInfo()
    }

    interface View : BaseView {

    }
}