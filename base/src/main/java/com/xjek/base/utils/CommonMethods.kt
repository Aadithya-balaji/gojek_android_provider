package com.xjek.base.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupWindow
import com.xjek.base.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
            val imageFileName: String = Constants.APPNAME + timeStamp + "_"
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (!storageDir.exists()) storageDir.mkdirs()
            return File.createTempFile(imageFileName, ".jpg", storageDir)
        }




        fun getDateinNeededFormat(strDate: String, fromSimpleFormat: SimpleDateFormat): Date {
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
                    Log.e("error",":------"+e.message)
                }
                val hoursFormat = SimpleDateFormat("HH:mm:ss")
                val formateDate = Date()
                formateDate.time = calendar!!.timeInMillis
                timeDiff=formateDate.time
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e("error",":------"+e.message)
            }
            return timeDiff!!

        }

        fun getTimeDifference(strFromDate:String,strToDate:String,format:String):Long{
            var diffTime:Long?=0
            val fromSimpledateFormat=SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
            fromSimpledateFormat.timeZone= TimeZone.getTimeZone("UTC")
            var fromDate:Date?=null
            var fromCalender :Calendar?=null
            var toDate:Date?=null
            var toCalendar:Calendar?=null
            try{
                fromDate=fromSimpledateFormat.parse(strFromDate)
                fromCalender= Calendar.getInstance(TimeZone.getDefault())
                fromCalender.time=fromDate

                toDate=fromSimpledateFormat.parse(strFromDate)
                toCalendar= Calendar.getInstance(TimeZone.getDefault())
                toCalendar.time=toDate
                diffTime=toCalendar.time.time -fromCalender
                        .time.time
                Log.e("fromTime","------"+fromCalender.time.time)
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
            return  diffTime!!

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