package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

data class VehicleResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>) : CommonResponse {
    data class ResponseData(
            @SerializedName("token_type")
            val tokenType: String,
            @SerializedName("expires_in")
            val expiresIn: Int,
            @SerializedName("access_token")
            val accessToken: String
    )
}