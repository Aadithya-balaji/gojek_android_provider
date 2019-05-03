package com.xjek.base.extensions

import androidx.lifecycle.ViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun ViewModel.createRequestBody(content: String): RequestBody {
    return RequestBody.create(MultipartBody.FORM, content)
}

fun ViewModel.createMultipartBody(
        content: String,
        contentType: String,
        file: File
): MultipartBody.Part {
    val requestFile = RequestBody.create(MediaType.parse(contentType), file)
    return MultipartBody.Part.createFormData(content, file.name, requestFile)
}