package com.gox.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import java.util.*

class LocaleUtils {

    object LANGUAGE {
        const val KEY = "language_key"
        const val DEFAULT = "en"
    }

    companion object {
        fun setLocale(mContext: Context): Context {
            return updateResources(mContext, getLanguagePref(mContext))
        }

        fun setNewLocale(mContext: Context, locale: String): Context {
            setLanguagePref(mContext, locale)
            return updateResources(mContext, locale)
        }

        @SuppressLint("NewApi")
        @SuppressWarnings("deprecation")
        private fun updateResources(context: Context, language: String?): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context.createConfigurationContext(configuration)
        }

        fun getLanguagePref(mContext: Context): String? {
            val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            return mPreferences.getString(LANGUAGE.KEY, LANGUAGE.DEFAULT)
        }

        private fun setLanguagePref(mContext: Context, locale: String) {
            val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            mPreferences.edit().putString(LANGUAGE.KEY, locale).apply()
        }
    }
}