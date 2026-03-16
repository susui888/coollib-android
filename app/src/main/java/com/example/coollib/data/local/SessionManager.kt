package com.example.coollib.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "coollib_session",
            Context.MODE_PRIVATE
        )

    fun saveToken(token: String, username: String) {
        prefs.edit {
            putString("token", token)
            putString("username",username)
        }
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun getUsername(): String? {
        return prefs.getString("username",null)
    }

    fun clearSession() {
        prefs.edit { clear() }
    }
}