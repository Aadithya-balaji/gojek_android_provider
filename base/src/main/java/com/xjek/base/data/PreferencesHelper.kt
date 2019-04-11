package com.xjek.base.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferencesHelper {

    fun getDefaultPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getCustomPreferences(context: Context, preferencesName: String): SharedPreferences {
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }
}