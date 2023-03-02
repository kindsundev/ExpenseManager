package com.kindsundev.expense.manager.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
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

fun Context.loadUserAvatar(link: String?, default: Int, target: ImageView) {
    Glide.with(this)
        .load(link)
        .placeholder(default)
        .error(default)
        .centerCrop()
        .into(target)
}