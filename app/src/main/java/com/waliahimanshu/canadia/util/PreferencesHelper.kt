package com.waliahimanshu.canadia.util

import android.content.Context
import android.content.SharedPreferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private const val PREF_CANADIA_PACKAGE_NAME = "com.waliahimanshu.canadia.preferences"
        private const val PREF_KEY_WALKTHROUGH_SHOWN: String = "PREF_KEY_WALKTHROUGH_SHOWN"

    }

    private val globalAppPref: SharedPreferences

    init {
        globalAppPref = context.getSharedPreferences(PREF_CANADIA_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

     var isWalkthroughShown: Boolean
        get() = globalAppPref.getBoolean(PREF_KEY_WALKTHROUGH_SHOWN, false)
        set(walkthroughShown) = globalAppPref.edit().putBoolean(PREF_KEY_WALKTHROUGH_SHOWN, walkthroughShown).apply()

}
