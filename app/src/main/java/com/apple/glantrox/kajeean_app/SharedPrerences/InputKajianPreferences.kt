package com.apple.glantrox.kajeean_app.SharedPrerences

import android.content.Context
import androidx.preference.PreferenceManager

class InputKajianPreferences(context: Context) {
    companion object {
        const val EXTRA_TIMESTAMP = "key_timestamp"
        const val EXTRA_JUDUL = "key_judul"
        const val EXTRA_LINKLIVE = "key_linklive"
        const val EXTRA_DESKRIPSI = "key_deskripsi"
        const val EXTRA_LINKPOSTER = "key_timestamp"
        const val EXTRA_ALAMATDETAIL = "key_alamatdetail"
        const val EXTRA_CITY = "key_city"
        const val EXTRA_DRAFT = "key_draft"
    }

        private val wkt = PreferenceManager.getDefaultSharedPreferences(context)
        var waktuKajian = wkt.getString(EXTRA_TIMESTAMP, "")
        set(value) = wkt.edit().putString(EXTRA_TIMESTAMP, value).apply()

        private val jdl = PreferenceManager.getDefaultSharedPreferences(context)
        var judulKajian  = jdl.getString(EXTRA_JUDUL, "")
        set(value) = jdl.edit().putString(EXTRA_JUDUL, value).apply()

        private val lv = PreferenceManager.getDefaultSharedPreferences(context)
        var linkLive = lv.getString(EXTRA_LINKLIVE, "")
        set(value) = lv.edit().putString(EXTRA_LINKLIVE, value).apply()

        private val dk = PreferenceManager.getDefaultSharedPreferences(context)
        var deskripsiKajian = dk.getString(EXTRA_DESKRIPSI, "")
        set(value) = dk.edit().putString(EXTRA_DESKRIPSI, value).apply()

        private val lp = PreferenceManager.getDefaultSharedPreferences(context)
        var linkPoster = lp.getString(EXTRA_LINKPOSTER, "")
        set(value) = lp.edit().putString(EXTRA_LINKPOSTER, value).apply()

        private val ad = PreferenceManager.getDefaultSharedPreferences(context)
        var alamatDetail = ad.getString(EXTRA_ALAMATDETAIL, "")
        set(value) = ad.edit().putString(EXTRA_ALAMATDETAIL, value).apply()

        private val ct = PreferenceManager.getDefaultSharedPreferences(context)
        var cityKajian = ct.getString(EXTRA_CITY, "")
        set(value) = ct.edit().putString(EXTRA_CITY, value).apply()

        private val idf = PreferenceManager.getDefaultSharedPreferences(context)
        var isDrafted = idf.getBoolean(EXTRA_DRAFT, false)
        set(value) = idf.edit().putBoolean(EXTRA_DRAFT, value).apply()
}