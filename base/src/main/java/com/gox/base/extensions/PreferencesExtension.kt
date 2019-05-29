package com.gox.base.extensions

import android.app.Service
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.gox.base.data.PreferencesHelper

val preferencesHelper = PreferencesHelper

inline fun <reified T> FragmentActivity.writePreferences(key: String, value: T) {
    preferencesHelper.put(key, value)
}

inline fun <reified T> Fragment.writePreferences(key: String, value: T) {
    preferencesHelper.put(key, value)
}

inline fun <reified T> Service.writePreferences(key: String, value: T) {
    preferencesHelper.put(key, value)
}

inline fun <reified T> ViewModel.writePreferences(key: String, value: T) {
    preferencesHelper.put(key, value)
}

inline fun <reified T> FragmentActivity.readPreferences(key: String): T {
    return preferencesHelper.get(key)
}

inline fun <reified T> FragmentActivity.readPreferences(key: String, value: T): T? {
    return preferencesHelper.get(key, value)
}

inline fun <reified T> Fragment.readPreferences(key: String): T {
    return preferencesHelper.get(key)
}

inline fun <reified T> Fragment.readPreferences(key: String, value: T): T? {
    return preferencesHelper.get(key, value)
}

inline fun <reified T> ViewModel.readPreferences(key: String): T {
    return preferencesHelper.get(key)
}

inline fun <reified T> ViewModel.readPreferences(key: String, value: T): T? {
    return preferencesHelper.get(key, value)
}

inline fun <reified T> Service.readPreferences(key: String, value: T): T? {
    return preferencesHelper.get(key, value)
}

fun FragmentActivity.clearPreferences() {
    preferencesHelper.removeAll()
}

inline fun <reified T> Fragment.clearPreferences() {
    preferencesHelper.removeAll()
}