package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

class ForgotPasswordResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            val username: String,
            @SerializedName("account_type")
            val accountType: String,
            val otp: String
    )
}