package com.xjek.base.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import com.xjek.base.base.BaseApplication
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


        private fun updateResources(context: Context, language: String?): Context {
            var mContext = context
            val locale = Locale(language)
            Locale.setDefault(locale)
            val res = mContext.resources
            val config = Configuration(res.configuration)
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale)
                mContext = mContext.createConfigurationContext(config)
            } else {
                config.locale = locale
                res.updateConfiguration(config, res.displayMetrics)
            }
            return context
        }

        fun getLanguagePref(mContext: Context): String? {
            val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            return mPreferences.getString(LANGUAGE.KEY, LANGUAGE.DEFAULT)
        }

        fun setLanguagePref(mContext: Context, locale: String) {
            val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            mPreferences.edit().putString(LANGUAGE.KEY,locale).apply()
        }
    }


}