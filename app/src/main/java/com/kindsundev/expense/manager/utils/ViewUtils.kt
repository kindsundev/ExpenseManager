package com.kindsundev.expense.manager.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletActivity
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

fun Context.startPrepareWalletActivity() =
    Intent(this, PrepareWalletActivity::class.java).also {
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

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.requestPremium() {
    val message = "You must update to premium to be using featured!!"
    showToast(message)
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
