package com.kindsundev.expense.manager.ui.home.menu.profile

import android.net.Uri
import com.kindsundev.expense.manager.ui.base.BaseView

interface ProfileContact {

    interface Presenter {
        fun updateProfile(uri: String?, name: String, email: String, password: String)
    }

    interface View : BaseView {

    }
}