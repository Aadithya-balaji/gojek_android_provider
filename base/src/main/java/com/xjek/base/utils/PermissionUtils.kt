package com.xjek.base.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class PermissionUtils {

    fun useRunTimePermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    fun hasPermission(activity: Activity, permission: String): Boolean {
        return !useRunTimePermissions()
                || activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hasPermission(activity: Activity, permissions: Array<String>): Boolean {
        for (permission in permissions) if (!hasPermission(activity, permission)) return false
        return true
    }

    fun requestPermissions(activity: FragmentActivity, permission: Array<String>, requestCode: Int): Boolean {
        if (useRunTimePermissions()) activity.requestPermissions(permission, requestCode)
        return hasPermission(activity, permission)
    }

    fun requestPermissions(fragment: Fragment, permission: Array<String>, requestCode: Int) {
        if (useRunTimePermissions()) fragment.requestPermissions(permission, requestCode)
    }

    fun shouldShowRational(activity: Activity, permission: String): Boolean {
        return useRunTimePermissions() && activity.shouldShowRequestPermissionRationale(permission)
    }

    fun shouldAskForPermission(activity: Activity, permission: String): Boolean {
        return useRunTimePermissions() && !hasPermission(activity, permission)
                && (!hasAskedForPermission(activity, permission) || shouldShowRational(activity, permission))
    }

    fun goToAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    fun hasAskedForPermission(activity: Activity, permission: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(permission, false)
    }

    fun markedPermissionAsAsked(activity: Activity, permission: String) {
        PreferenceManager
                .getDefaultSharedPreferences(activity)
                .edit()
                .putBoolean(permission, true)
                .apply()
    }
}
