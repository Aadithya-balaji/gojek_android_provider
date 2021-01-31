package com.gox.base.extensions

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun createRequestBody(content: String): RequestBody {
    return content.toRequestBody(MultipartBody.FORM)
}

fun createMultipartBody(
        content: String,
        contentType: String,
        file: File
): MultipartBody.Part {
    val requestFile = file.asRequestBody(contentType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(content, file.name, requestFile)
}