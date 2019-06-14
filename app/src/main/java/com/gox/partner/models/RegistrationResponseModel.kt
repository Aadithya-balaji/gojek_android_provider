package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegistrationResponseModel(
        @SerializedName("statusCode")
        @Expose
        var statusCode: String? = null,
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("message")
        @Expose
        var message: String? = null,
        @SerializedName("responseData")
        @Expose
        var responseData: LoginResponseModel.ResponseData? = null,
        @SerializedName("error")
        @Expose
        var error: List<Any>? = null
)