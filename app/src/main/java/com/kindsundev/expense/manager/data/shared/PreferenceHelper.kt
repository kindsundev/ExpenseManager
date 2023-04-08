package com.kindsundev.expense.manager.data.shared

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    private const val PREFERENCES_KEY = "MY_LOCAL_SHARED_PREFERENCES"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    // save preferences

    fun setInteger(appContext: Context, key: String?, value: Int) =
        getPreferences(appContext).edit().putInt(key, value).apply()

    fun setFloat(appContext: Context, key: String?, value: Float) =
        getPreferences(appContext).edit().putFloat(key, value).apply()

    fun setString(appContext: Context, key: String?, value: String) =
        getPreferences(appContext).edit().putString(key, value).apply()

    fun setBoolean(appContext: Context, key: String?, value: Boolean) =
        getPreferences(appContext).edit().putBoolean(key, value).apply()

    // retrieve preferences

    fun getInteger(appContext: Context, key: String?, defaultValue: Int?) =
        getPreferences(appContext).getInt(key, defaultValue!!)

    fun getFloat(appContext: Context, key: String?, defaultValue: Float?) =
        getPreferences(appContext).getFloat(key, defaultValue!!)

    fun getString(appContext: Context, key: String?, defaultValue: String?) =
        getPreferences(appContext).getString(key, defaultValue!!)

    fun getBoolean(appContext: Context, key: String?, defaultValue: Boolean?) =
        getPreferences(appContext).getBoolean(key, defaultValue!!)
}