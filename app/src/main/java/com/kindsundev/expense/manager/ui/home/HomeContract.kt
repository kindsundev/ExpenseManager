package com.kindsundev.expense.manager.ui.home

import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.ui.base.BaseView

interface HomeContract {

    interface Presenter {
        fun getCurrentUser() : UserModel
    }

    interface View : BaseView {

    }
}