package com.kindsundev.expense.manager.ui.home.bag.notification

import com.kindsundev.expense.manager.ui.base.BaseView

interface NotificationContract {
    interface Presenter {

    }

    interface View : BaseView

    interface Listener {
        fun onClickNotificationItem(message: String)
    }
}