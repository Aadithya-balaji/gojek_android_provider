package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

class ResetPasswordResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            @SerializedName("base_url")
            val baseUrl: String
    )
}