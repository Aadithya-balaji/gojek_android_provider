package com.xjek.provider.models

import com.google.gson.annotations.SerializedName

data class ReferalDataModel(
        @SerializedName("referral_code")
        var referralCode: String? = null,
        @SerializedName("referral_amount")
        var referalAmount: String,
        @SerializedName("referral_count")
        var referralCount: String? = null,
        @SerializedName("user_referral_count")
        var userReferralCount: String? = null,
        @SerializedName("user_referral_amount")
        var userReferralAmount: Double
)