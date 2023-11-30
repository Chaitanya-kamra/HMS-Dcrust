package com.chaitanya.hms_dcrust.utils

import android.content.Context

object TokenManager {

    private const val AUTH_TOKEN_KEY = "auth_token"

    fun saveAuthToken(context: Context, authToken: String) {
        val sharedPrefs = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(AUTH_TOKEN_KEY, authToken).apply()
    }

    fun getAuthToken(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        return sharedPrefs.getString(AUTH_TOKEN_KEY, null)
    }

    fun clearAuthToken(context: Context) {
        val sharedPrefs = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(AUTH_TOKEN_KEY).apply()
    }
}