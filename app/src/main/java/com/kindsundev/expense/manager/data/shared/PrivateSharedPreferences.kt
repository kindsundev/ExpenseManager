package com.kindsundev.expense.manager.data.shared

import android.content.Context

class PrivateSharedPreferences(context: Context) {
    private val file = "ID_TOKEN_USER_LOGGED_FILE"
    private val key = "ID_TOKEN_USER_LOGGED_KEY"
    private val sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveUserIdTokenLogged(token : String?) {
        editor.putString(key, token)
        editor.apply()
    }

    fun getUserIdTokenLogged(): String? = sharedPreferences.getString(key, "null")
}