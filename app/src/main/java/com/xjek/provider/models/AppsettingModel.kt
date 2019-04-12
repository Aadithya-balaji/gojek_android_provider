package com.xjek.provider.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import org.intellij.lang.annotations.Language

class AppsettingModel {
    @SerializedName("referral")
    @Expose
    var referral: Int? = null
    @SerializedName("social_login")
    @Expose
    var socialLogin: Int? = null
    @SerializedName("otp_verify")
    @Expose
    var otpVerify: Int? = null
    @SerializedName("cmspage")
    @Expose
    var cmspage: CmspageModel? = null
    @SerializedName("supportdetails")
    @Expose
    var supportdetails: SupportdetailsModel? = null
    @SerializedName("languages")
    @Expose
    var languages: List<LanguageModel>? = null

    data class LanguageModel(
            val name: String,
            val key: String
    )
}
