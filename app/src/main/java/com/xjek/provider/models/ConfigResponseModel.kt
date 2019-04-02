package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

data class ConfigResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            @SerializedName("base_url")
            val baseUrl: String,
            val services: List<Service>
    ) {
        data class Service(
                val id: Int,
                @SerializedName("admin_service_name")
                val adminServiceName: String,
                @SerializedName("base_url")
                val baseUrl: String,
                val status: Int,
                @SerializedName("company_id")
                val companyId: Int
        )
    }
}