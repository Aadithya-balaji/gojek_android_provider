package com.xjek.provider.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.util.regex.Pattern

class CommanMethods {
    companion object {
        fun getDefaultFileName(context: Context): File {
            var imageFile: File? = null
            val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            if (isSDPresent) { // External storage path
                imageFile = File(Environment.getExternalStorageDirectory().toString() + File.separator + Constant.TEMP_FILE_NAME + System.currentTimeMillis() + ".png")
            } else {  // Internal storage path
                imageFile = File(context.filesDir.toString() + File.separator + Constant.TEMP_FILE_NAME + System.currentTimeMillis() + ".png")
            }
            return imageFile
        }

        fun validateEmail(email: String): Boolean {
            val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            return email.length > 0 && matcher.matches()
        }

        fun validatePhone(phone: String): Boolean {
            if (phone.length >= 10) {
                Log.e("valid","---"+phone)
                return true
            } else {
                return false
            }
        }
    }
}