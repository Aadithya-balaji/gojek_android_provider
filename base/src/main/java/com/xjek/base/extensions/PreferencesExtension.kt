package com.xjek.base.extensions

import android.content.SharedPreferences

const val message = "Value type is undefined"

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = this.edit()
    when (T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> throw UnsupportedOperationException(message)
    }
    editor.apply()
}

inline fun <reified T> SharedPreferences.get(key: String): T {
    return when (T::class) {
        Boolean::class -> this.getBoolean(key, false) as T
        Float::class -> this.getFloat(key, -1f) as T
        Int::class -> this.getInt(key, -1) as T
        Long::class -> this.getLong(key, -1L) as T
        String::class -> this.getString(key, null) as T
        else -> throw UnsupportedOperationException(message)
    }
}

inline fun <reified T> SharedPreferences.get(key: String, defValue: T? = null): T? {
    return when (T::class) {
        Boolean::class -> this.getBoolean(key, defValue as? Boolean ?: false) as T?
        Float::class -> this.getFloat(key, defValue as? Float ?: -1f) as T?
        Int::class -> this.getInt(key, defValue as? Int ?: -1) as T?
        Long::class -> this.getLong(key, defValue as? Long ?: -1L) as T?
        String::class -> this.getString(key, defValue as? String) as T?
        else -> throw UnsupportedOperationException(message)
    }
}

fun SharedPreferences.removeAll() {
    this.edit().clear().apply()
}