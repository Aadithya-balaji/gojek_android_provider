package com.gox.base.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.*

object ImageUtils {

    fun getPathFromInputStreamUri(context:Context,uri: Uri): Uri? {
        return Uri.fromFile(getPathFromInputStreamFile(context,uri))
    }

    fun getPathFromInputStreamFile(context: Context,uri: Uri):File?{
        fun ContentResolver.getCursorContent(uri: Uri): String? = kotlin.runCatching {
            query(uri, null, null, null, null)?.use { cursor ->
                val nameColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    cursor.getString(nameColumnIndex)
                } else null
            }
        }.getOrNull()

        fun getFileName(uri: Uri): String? {
            when (uri.scheme) {
                ContentResolver.SCHEME_FILE -> {
                    val filePath = uri.path
                    if (!filePath.isNullOrEmpty()) {
                        return File(filePath).name
                    }
                }

                ContentResolver.SCHEME_CONTENT -> {
                    return context.contentResolver.getCursorContent(uri)
                }
            }
            return null
        }

        fun createTemporalFile(fileName: String?): File {
            return File(context.cacheDir, fileName ?: "tempPicture.jpg")
        }

        @Throws(IOException::class)
        fun createTemporalFileFrom(inputStream: InputStream?, fileExtension: String?): File? {
            var targetFile: File? = null
            return if (inputStream == null) targetFile
            else {
                var read: Int
                val buffer = ByteArray(8 * 1024)
                targetFile = createTemporalFile(fileExtension)
                FileOutputStream(targetFile).use { out ->
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        out.write(buffer, 0, read)
                    }
                    out.flush()
                }
                targetFile
            }
        }

        var filePath: File? = null
        uri.authority?.let {
            try {
                context.contentResolver.openInputStream(uri).use {
                    val photoFile: File? = createTemporalFileFrom(it, getFileName(uri))
                    filePath = photoFile
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return filePath
    }

}