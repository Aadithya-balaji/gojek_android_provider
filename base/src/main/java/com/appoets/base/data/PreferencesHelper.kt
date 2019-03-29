package com.appoets.base.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferencesHelper {

    fun getDefaultSharedPreferences(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    fun getPreferences(context: Context, fileName: String): SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    fun SharedPreferences.setValue(key: String, value: Any) {
        when (value) {
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            is String -> edit({ it.putString(key, value) })
            else -> throw UnsupportedOperationException("Value type is undefined")
        }
    }

    inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            String::class -> getString(key, defaultValue as? String) as T?
            else -> throw UnsupportedOperationException("Value type is undefined")
        }
    }
}