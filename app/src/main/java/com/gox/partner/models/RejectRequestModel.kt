package com.gox.partner.models

import java.io.Serializable

data class RejectRequestModel(
        var error: List<Any?>?,
        var message: String?,
        var responseData: Any?,
        var statusCode: String?,
        var title: String?
) : Serializable