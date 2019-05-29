package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class CommonResponse(
        @SerializedName("statusCode")
        var statusCode: String? = "",
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("responseData")
        var responseData: List<Object>? = null,
        @SerializedName("error")
        var error: List<Object>? = null
)