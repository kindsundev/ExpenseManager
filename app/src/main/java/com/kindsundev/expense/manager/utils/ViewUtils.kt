package com.kindsundev.expense.manager.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.signin.SignInActivity

fun Context.startSignInActivity() =
    Intent(this, SignInActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startHomeActivity() =
    Intent(this, HomeActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.onFeatureIsDevelop() {
    Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show()
}

fun startLoadingDialog(progress: LoadingDialog, manager: FragmentManager, status: Boolean) {
    if (status) {
        progress.show(manager, Constant.LOADING_DIALOG_NAME)
    } else {
        progress.dismiss()
    }
}