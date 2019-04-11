package com.xjek.provider.models

import com.xjek.base.models.CommonResponse

data class ManageServicesResponseModel(
        val responseData: List<ResponseData>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            val id: Int,
            val adminServiceName: String,
            val displayName: String,
            val baseUrl: String,
            val status: Int,
            val providerservices: Providerservices
    ) {
        data class Providerservices(
                val id: Int,
                val providerId: Int,
                val adminServiceId: Int,
                val providerVehicleId: Any,
                val rideDeliveryId: Any,
                val serviceId: Any,
                val categoryId: Int,
                val subCategoryId: Int,
                val companyId: Int,
                val baseFare: String,
                val perMiles: String,
                val perMins: String,
                val status: String
        )
    }
}