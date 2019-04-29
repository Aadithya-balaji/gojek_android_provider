package com.xjek.base.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class  CommonMethods {
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