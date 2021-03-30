package com.canhub.cropper.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.canhub.cropper.common.CommonValues
import com.canhub.cropper.common.CommonVersionCheck
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

/**
 * This class exist because of two issues. One is related to the new Scope Storage for OS 10+
 * Where we should not access external storage anymore. Because of this we cannot get a external uri
 *
 * Using FileProvider to retrieve the path can return a value that is not the real one for some devices
 * This happen in specific devices and OSs. Because of this is needed to do a lot of if/else and
 * try/catch to just use the latest cases when need.
 *
 * This code is not good, but work. I don't suggest anyone to reproduce it.
 *
 * Most of the devices will work fine, but if you worry about memory usage, please remember to clean
 * the cache from time to time,
 */
internal fun getUriForFile(context: Context, file: File): Uri {
    val authority = context.packageName + CommonValues.authority
    try {
        Log.i("CropLibGetUri", "Try get URI for scope storage - content://")
        return FileProvider.getUriForFile(context, authority, file)
    } catch (e: Exception){
        Log.e("crop","------"+e.message)
    }
    return FileProvider.getUriForFile(context, authority, file)
}
