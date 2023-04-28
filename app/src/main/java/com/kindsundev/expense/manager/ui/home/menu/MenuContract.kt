package com.kindsundev.expense.manager.ui.home.menu

import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface MenuContract {

    interface Presenter {
        fun getCurrentUser() : UserModel
    }

    interface View : BaseView
}