package com.apple.glantrox.kajeean_app.SharedPrerences

import android.content.Context
import android.preference.PreferenceManager

class GeneralPreferences(context: Context) {

    companion object {
        const val LOGIN_KEY = "user_id"
        const val KAJIAN_KEY = "kajian_id"
    }

    // Logged-in User ID
    private val currentUserId = PreferenceManager.getDefaultSharedPreferences(context)
    var UserID = currentUserId.getInt(LOGIN_KEY, -1)
    set(value) = currentUserId.edit().putInt(LOGIN_KEY, value).apply()

    // Kajian ID
    private val currentKajianId = PreferenceManager.getDefaultSharedPreferences(context)
    var KajianID = currentKajianId.getInt(KAJIAN_KEY, -1)
    set(value) = currentKajianId.edit().putInt(KAJIAN_KEY, value).apply()
    


}