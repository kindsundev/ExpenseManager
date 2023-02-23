package com.kindsundev.expense.manager.utils

import android.content.Context
import android.content.Intent
import com.kindsundev.expense.manager.view.home.HomeActivity

fun Context.startHomeActivity() =
    Intent(this, HomeActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }