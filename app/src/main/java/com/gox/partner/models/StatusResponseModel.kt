package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class StatusResponseModel(
        @SerializedName("statusCode")
        var statusCode: String? = "",
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("responseData")
        var responseData: StatusResponseDataModel? = null,
        @SerializedName("error")
        var error: List<Any>? = null
)