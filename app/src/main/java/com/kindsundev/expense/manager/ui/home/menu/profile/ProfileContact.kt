package com.kindsundev.expense.manager.ui.home.menu.profile

import android.net.Uri
import com.kindsundev.expense.manager.ui.base.BaseView

interface ProfileContact {

    interface Presenter {
        fun updateProfile(uri: Uri?, name: String)

        fun updatePassword(password: String)
    }

    interface View : BaseView {

    }
}