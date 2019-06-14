package com.gox.partner.models

import com.google.gson.annotations.SerializedName
import com.gox.base.models.CommonResponse

data class VehicleCategoryResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            val transport: List<Transport>
    ) {
        data class Transport(
                val id: Int,
                @SerializedName("vehicle_name") val vehicleName: String,
                @SerializedName("estimated_time") val estimatedTime: String
        ) {
            override fun toString(): String {
                return vehicleName
            }
        }
    }
}