package com.waliahimanshu.canadia.util

import android.content.Context
import android.content.SharedPreferences

import javax.inject.Inject
import javax.inject.Singleton

/**
 * General Preferences Helper class, used for storing preference values using the Preference API
 */
@Singleton
class PreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private val PREF_CANADIA_PACKAGE_NAME = "com.waliahimanshu.canadia.preferences"

        private val PREF_KEY_LAST_CACHE = "last_cache"
    }

    private val bufferPref: SharedPreferences

    init {
        bufferPref = context.getSharedPreferences(PREF_CANADIA_PACKAGE_NAME, Context.MODE_PRIVATE)
    }


    var lastCacheTime: Long
        get() = bufferPref.getLong(PREF_KEY_LAST_CACHE, 0)
        set(lastCache) = bufferPref.edit().putLong(PREF_KEY_LAST_CACHE, lastCache).apply()

}