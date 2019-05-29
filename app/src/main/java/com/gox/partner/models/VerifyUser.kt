package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyUser {

    @SerializedName("statusCode")
    @Expose
    var statusCode: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("responseData")
    @Expose
    var responseData: List<Any>? = null
    @SerializedName("error")
    @Expose
    var error: List<Any>? = null

}