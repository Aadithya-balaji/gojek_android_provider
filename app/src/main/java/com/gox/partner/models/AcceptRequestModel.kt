package com.gox.partner.models

import java.io.Serializable

data class AcceptRequestModel(
        var error: List<Any?>?,
        var message: Any?,
        var responseData: Any?,
        var statusCode: String?,
        var title: String?
) : Serializable