package com.example.coollib.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SessionManager @Inject constructor(
    context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "coollib_session",
            Context.MODE_PRIVATE
        )

    fun saveToken(token: String) {
        prefs.edit {
            putString("token", token)
        }
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun clearSession() {
        prefs.edit { clear() }
    }
}