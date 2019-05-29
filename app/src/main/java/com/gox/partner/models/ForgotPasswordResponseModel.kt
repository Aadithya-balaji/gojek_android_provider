package com.gox.partner.models

import com.google.gson.annotations.SerializedName
import com.gox.base.models.CommonResponse

class ForgotPasswordResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            @SerializedName("account_type") val accountType: String,
            @SerializedName("country_code") val countryCode: String,
            val username: String,
            val otp: String
    )
}