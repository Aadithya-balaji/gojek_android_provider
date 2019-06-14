package com.gox.base.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gox.base.base.BaseApplication
import javax.inject.Inject

class RunTimePermission(context: Context) {

    @Inject
    lateinit var permissionList: ArrayList<String>

    private lateinit var preferences: SharedPreferences
    private lateinit var context: Context

    init {
        BaseApplication.appController!!.inject(this)
        this.context = context
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setFirstTimePermission(isFirstTime: Boolean) {
        preferences.edit().putBoolean("isFirstTimePermission", isFirstTime).apply()
    }

    fun isFirstTimePermission(): Boolean {
        return preferences.getBoolean("isFirstTimePermission", false)
    }

    fun checkHasPermission(context: Activity?, permissions: Array<String>?): java.util.ArrayList<String> {
        permissionList.clear()
        if (isMarshmallow() && context != null && permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !== PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission)
                }
            }
        }
        return permissionList
    }

    fun isPermissionBlocked(context: Activity?, permissions: java.util.ArrayList<String>): Boolean {
        if (isMarshmallow() && context != null && permissions != null && isFirstTimePermission()) {
            for (permission in permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    return true
                }
            }
        }
        return false
    }

    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray?): java.util.ArrayList<String> {
        permissionList.clear()
        if (grantResults != null && grantResults.size > 0) {
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i])
                }
            }
        }
        return permissionList
    }

    private fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

}