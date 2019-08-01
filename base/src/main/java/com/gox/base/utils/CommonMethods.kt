package com.gox.base.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.gox.base.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CommonMethods {
    companion object {

        fun refreshGallery(context: Context, file: File?) {
            try {
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = Uri.fromFile(file) //out is your file you saved/deleted/moved/copied
                context.sendBroadcast(mediaScanIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun createImageFile(context: Context): File {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName: String = context.getString(R.string.app_name) + timeStamp + "_"
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (!storageDir.exists()) storageDir.mkdirs()
            return File.createTempFile(imageFileName, ".jpg", storageDir)
        }

        fun checkGps(context: Context) {
            val locationRequest = LocationRequest.create()
            locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest?.interval = 1000
            locationRequest?.numUpdates = 1
            locationRequest?.fastestInterval = (3 * 1000).toLong()
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

            val settingsBuilder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest!!)
            settingsBuilder.setAlwaysShow(true)
            val result = LocationServices.getSettingsClient(context)
                    .checkLocationSettings(settingsBuilder.build())
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(ApiException::class.java)
                } catch (ex: ApiException) {
                    when (ex.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                            val permissionLocation = ContextCompat.checkSelfPermission(context,
                                    Manifest.permission.ACCESS_FINE_LOCATION)
                            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                            }
                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = ex as ResolvableApiException
                            resolvableApiException.startResolutionForResult(context as AppCompatActivity, 500)
                        } catch (e: IntentSender.SendIntentException) {

                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            }
        }

        fun thousandSeparator(cost: Double): String {
            var value = ""
            try {
                value = String.format(Locale.getDefault(), "%,.2f", cost)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }

        fun getDateNeededFormat(strDate: String, fromSimpleFormat: SimpleDateFormat): Date {
            val date: Date = fromSimpleFormat.parse(strDate)
            return date
        }

        fun getLocalTime(strDate: String, dateFormat: String): Long {
            var timeDiff: Long? = 0
            try {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                df.timeZone = TimeZone.getTimeZone("UTC")
                var date: Date? = null
                var calendar: Calendar? = null
                try {
                    date = df.parse(strDate)
                    calendar = Calendar.getInstance(TimeZone.getDefault())
                    calendar.time = date
                } catch (e: ParseException) {
                    e.printStackTrace()
                    Log.e("error", ":------" + e.message)
                }
                val formateDate = Date()
                formateDate.time = calendar!!.timeInMillis
                timeDiff = formateDate.time
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e("error", ":------" + e.message)
            }
            return timeDiff!!

        }

        fun getTimeDifference(strFromDate: String, strToDate: String, format: String): String {
            val diffTime: Long?
            var hms: String? = ""
            val fromSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            fromSimpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val fromDate: Date?
            val fromCalender: Calendar?
            val toDate: Date?
            val toCalendar: Calendar?
            try {
                fromDate = fromSimpleDateFormat.parse(strFromDate)
                fromCalender = Calendar.getInstance(TimeZone.getDefault())
                fromCalender.time = fromDate
                toDate = fromSimpleDateFormat.parse(strToDate)
                toCalendar = Calendar.getInstance(TimeZone.getDefault())
                toCalendar.time = toDate
                diffTime = toCalendar.time.time - fromCalender.time.time
                hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diffTime),
                        TimeUnit.MILLISECONDS.toMinutes(diffTime) - TimeUnit.HOURS
                                .toMinutes(TimeUnit.MILLISECONDS.toHours(diffTime)),
                        TimeUnit.MILLISECONDS.toSeconds(diffTime) - TimeUnit.MINUTES
                                .toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffTime)))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return hms!!
        }

        fun decodeSampledBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap? { // BEST QUALITY MATCH
            val orientation: Int
            try {
                // First decode with inJustDecodeBounds=true to check dimensions
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(path, options)

                // Calculate inSampleSize
                // Raw height and width of image
                val height = options.outHeight
                val width = options.outWidth
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                var inSampleSize = 1

                if (height > reqHeight) {
                    inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
                }
                val expectedWidth = width / inSampleSize
                if (expectedWidth > reqWidth) {
                    //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
                    inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
                }
                options.inSampleSize = inSampleSize
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false
                val bm = BitmapFactory.decodeFile(path, options)
                var bitmap = bm
                val exif = ExifInterface(path)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                val m = Matrix()

                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    m.postRotate(180f)
                    //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                    // if(m.preRotate(90)){
                    //Log.e("in orientation", "" + orientation);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
                    return bitmap
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    m.postRotate(90f)
                    //Log.e("in orientation", "" + orientation);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
                    return bitmap
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    m.postRotate(270f)
                    //Log.e("in orientation", "" + orientation);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
                    return bitmap
                }
                return bitmap
            } catch (e: Exception) {
                return null
            }
        }
    }
}