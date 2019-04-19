package com.xjek.provider.models

import java.io.Serializable

data class AcceptRequestModel(
        var error: List<Any?>?,
        var message: String?,
        var responseData: Any?,
        var statusCode: String?,
        var title: String?
) : Serializable