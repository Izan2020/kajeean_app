package com.apple.glantrox.kajeean_app.SharedPrerences

import android.content.Context
import android.preference.PreferenceManager

class UrlPreferences(context: Context) {

    companion object {
        const val PFP_KEY = "pfp_url"
    }

    // URL Profile Pictures
    private val currentPfp = PreferenceManager.getDefaultSharedPreferences(context)
    var pfpUrl = currentPfp.getString(PFP_KEY, "")
    set(value) = currentPfp.edit().putString(PFP_KEY, value).apply()

    // URL Poster Kajian
    private val currentPoster = PreferenceManager.getDefaultSharedPreferences(context)
    var PosterKajianUrl = currentPoster.getString(PFP_KEY, "")
    set(value) = currentPoster.edit().putString(PFP_KEY, value).apply()

}