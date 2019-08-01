package com.gox.base.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gox.base.R
import com.gox.base.data.PreferencesHelper.preferences

class PermissionUtils {

    var isFirstTimePermission: Boolean
        get() = preferences.getBoolean("isFirstTimePermission", false)
        set(isFirstTime) = preferences.edit().putBoolean("isFirstTimePermission", isFirstTime).apply()

    fun useRunTimePermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    fun hasPermission(activity: Activity, permission: String): Boolean {
        return !useRunTimePermissions()
                || activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private val isMarshmallow: Boolean
        get() = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) or (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1)

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

    private fun callPermissionSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        val appCompatActivity = context as AppCompatActivity
        appCompatActivity.startActivityForResult(intent, 300)
    }

    fun hasAskedForPermission(activity: Activity, permission: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(permission, false)
    }

    fun hasAllPermission(permission: Array<String>, context: Context, requestCode: Int): Boolean {
        var isPermissionNeed = false
        val blockedPermission = checkHasPermission(context as AppCompatActivity, permission)
        if (blockedPermission.size > 0) {
            val isBlocked = isPermissionBlocked(context, blockedPermission)
            if (isBlocked) {
                showMessageOKCancel(context, DialogInterface.OnClickListener { dialog, which -> callPermissionSettings(context) })
            } else ActivityCompat.requestPermissions(context, permission, requestCode)
        } else isPermissionNeed = true
        return isPermissionNeed
    }

    fun checkHasPermission(context: AppCompatActivity?, permissions: Array<String>?): ArrayList<String> {
        val permissionList = ArrayList<String>()
        if (isMarshmallow && context != null && permissions != null)
            for (permission in permissions)
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    permissionList.add(permission)
        return permissionList
    }

    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray?): Array<String> {
        val permissionList = ArrayList<String>()
        if (grantResults != null && grantResults.isNotEmpty())
            for (i in permissions.indices)
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    permissionList.add(permissions[i])
        return permissionList.toTypedArray()
    }

    fun isPermissionBlocked(context: Activity?, permissions: ArrayList<String>?): Boolean {
        if (isMarshmallow && context != null && permissions != null && isFirstTimePermission)
            for (permission in permissions)
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission))
                    return true
        return false
    }

    private fun showMessageOKCancel(context: Context, okListener: DialogInterface.OnClickListener) {
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(context)
        val li = LayoutInflater.from(context)
        val dialogView = li.inflate(R.layout.layout_permission_setting, null)
        dialogBuilder.setView(dialogView)
        val tvOk = dialogView.findViewById(R.id.tvOk) as TextView
        tvOk.setOnClickListener { v ->
            okListener.onClick(dialog, v!!.id)
            dialog!!.dismiss()
        }
        val tvCancel = dialogView.findViewById(R.id.tvCancel) as TextView
        tvCancel.setOnClickListener { dialog!!.dismiss() }

        dialog = dialogBuilder.create()
        dialog.show()
    }
}