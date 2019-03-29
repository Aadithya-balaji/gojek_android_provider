package com.appoets.base.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import com.appoets.base.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import es.dmoral.toasty.Toasty
import permissions.dispatcher.PermissionRequest

object ViewUtils {

    @MainThread
    fun showToast(context: Context, message: String, isSuccess: Boolean) {
        if (isSuccess)
            Toasty.success(context, message, Toast.LENGTH_SHORT).show()
        else
            Toasty.error(context, message, Toast.LENGTH_SHORT).show()
    }

    @MainThread
    fun showNormalToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @MainThread
    fun showAlert(context: Context, message: String, callBack: ViewCallBack) {
        AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    callBack.onPositiveButtonClick(dialog)
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    callBack.onNegativeButtonClick(dialog)
                }.show()
    }

    @MainThread
    fun showAlert(context: Context, message: String) {
        AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes) { dialog, which -> dialog.dismiss() }
                .show()
    }

    @MainThread
    fun showRationaleAlert(context: Context, message: String, request: PermissionRequest) {
        AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(context.resources.getString(R.string.action_allow)) { dialog, which ->
                    request.proceed()
                }
                .setNegativeButton(context.resources.getString(R.string.action_deny)) { dialog, which ->
                    request.cancel()
                }.show()
    }

    fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    interface ViewCallBack {
        fun onPositiveButtonClick(dialog: DialogInterface)
        fun onNegativeButtonClick(dialog: DialogInterface)
    }
}
