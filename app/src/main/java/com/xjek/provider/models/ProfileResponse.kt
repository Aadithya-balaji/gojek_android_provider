package com.xjek.provider.models

import com.google.gson.annotations.SerializedName

class ProfileResponse {
    @SerializedName("statusCode")
    var statusCode: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("responseData")
    var profileData: ProfileData? = null
}