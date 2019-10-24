package com.gox.base.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.gox.base.R
import es.dmoral.toasty.Toasty
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ViewUtils {

    private var dialog: Dialog? = null

    fun showSoftInputWindow(activity: Activity) {
        val manager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftInputWindow(activity: Activity) {
        println("RRR :: ViewUtils.hideSoftInputWindow")
        val manager = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideSoftInputWindow(context: Context, view: View) {
        val manager = context
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @MainThread
    fun showToast(context: Context, message: String, isSuccess: Boolean) {
        if (isSuccess) Toasty.success(context, message, Toast.LENGTH_SHORT).show()
        else Toasty.error(context, message, Toast.LENGTH_SHORT).show()
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
                }.setNegativeButton(android.R.string.no) { dialog, which ->
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
    fun showAlert(context: Context, message: String, positiveText: CharSequence,
                  negativeText: CharSequence, callBack: ViewCallBack) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(positiveText) { dialog, which ->
                    run {
                        dialog.dismiss()
                        callBack.onPositiveButtonClick(dialog)
                    }
                }
                .setNegativeButton(negativeText) { dialog, which ->
                    run {
                        dialog.dismiss()
                        callBack.onNegativeButtonClick(dialog)
                    }
                }

        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        val tvTitle:TextView = dialog.findViewById(R.id.alertTitle)!!
        val tvMessage:TextView = dialog.findViewById(android.R.id.message)!!
        val positiveText:Button = dialog.findViewById(android.R.id.button1)!!
        val negativeText:Button = dialog.findViewById(android.R.id.button2)!!

        val typeface = ResourcesCompat.getFont(context, R.font.avenir_lt_std_medium);

        tvTitle.typeface = typeface
        tvMessage.typeface = typeface
        positiveText.typeface = typeface
        negativeText.typeface = typeface
    }

    fun showGpsDialog(context: Context) {
        dialog = Dialog(context, R.style.DialogCustomTheme)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.layout_enable_gbs)
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.show()
    }

    fun dismissGpsDialog() {
        if (dialog != null)
            dialog!!.dismiss()
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

    fun getTimeDifference(date: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")

        try {
            val date1 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().time))
            val date2 = simpleDateFormat.parse(date)

            var different = date1.time - date2.time
            val diffInMill = different * 1000

            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24


            val elapsedDays = different / daysInMilli
            different = different % daysInMilli

            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli

            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli

            val elapsedSeconds = different / secondsInMilli

            println("SK_TEST_TIMES_AGO $elapsedDays days , $elapsedHours hours , $elapsedMinutes mins , $elapsedSeconds seconds")

            return if (diffInMill < secondsInMilli) {
                "just now"
            } else if (diffInMill < minutesInMilli) {
                if (elapsedSeconds > 1)
                    "$elapsedSeconds secs ago"
                else
                    "$elapsedSeconds sec ago"
            } else if (diffInMill < hoursInMilli) {
                if (elapsedMinutes > 1)
                    "$elapsedMinutes mins ago"
                else
                    "$elapsedMinutes min ago"
            } else if (diffInMill < daysInMilli) {
                if (elapsedHours > 1)
                    "$elapsedHours hours ago"
                else
                    "$elapsedHours hour ago"
            } else {
                if (elapsedDays > 1)
                    "$elapsedDays days ago"
                else
                    "$elapsedDays day ago"
            }


        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "0"
    }

    fun showMessageOKCancel(context: Context, message: String, okListener: DialogInterface.OnClickListener) {
        var dialog: android.app.AlertDialog? = null
        val dialogBuilder = android.app.AlertDialog.Builder(context)
        val li = LayoutInflater.from(context)
        val dialogView = li.inflate(R.layout.layout_permission_setting, null)
        dialogBuilder.setView(dialogView)
        val tvMessage = dialogView.findViewById(R.id.tvMessage) as TextView
        tvMessage.text = message
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
