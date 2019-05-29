package com.gox.base.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferencesHelper {

    const val message = "Value type is undefined"
    private lateinit var preferencesInstance: SharedPreferences

    val preferences: SharedPreferences
        get() = preferencesInstance

    fun setDefaultPreferences(context: Context) {
        preferencesInstance = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setCustomPreferences(context: Context, preferencesName: String) {
        preferencesInstance = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    inline fun <reified T> put(key: String, value: T) {
        val editor = preferences.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            Float::class -> editor.putFloat(key, value as Float)
            Int::class -> editor.putInt(key, value as Int)
            Long::class -> editor.putLong(key, value as Long)
            String::class -> editor.putString(key, value as String)
            Set::class -> editor.putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException(message)
        }
        editor.apply()
    }

    inline fun <reified T> get(key: String): T {
        return when (T::class) {
            Boolean::class -> preferences.getBoolean(key, false) as T
            Float::class -> preferences.getFloat(key, -1f) as T
            Int::class -> preferences.getInt(key, -1) as T
            Long::class -> preferences.getLong(key, -1L) as T
            String::class -> preferences.getString(key, "") as T
            Set::class -> preferences.getStringSet(key, null) as T
            else -> throw UnsupportedOperationException(message)
        }
    }

    inline fun <reified T> get(key: String, defValue: T? = null): T? {
        return when (T::class) {
            Boolean::class -> preferences.getBoolean(key, defValue as? Boolean ?: false) as T?
            Float::class -> preferences.getFloat(key, defValue as? Float ?: -1f) as T?
            Int::class -> preferences.getInt(key, defValue as? Int ?: -1) as T?
            Long::class -> preferences.getLong(key, defValue as? Long ?: -1L) as T?
            String::class -> preferences.getString(key, defValue as? String) as T?
            Set::class -> preferences.getStringSet(key, defValue as? Set<String>) as T?
            else -> throw UnsupportedOperationException(message)
        }
    }

    fun removeAll() {
        preferences.edit().clear().apply()
    }
}