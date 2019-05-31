package com.gox.partner.models

import com.google.gson.annotations.SerializedName
import com.gox.base.models.CommonResponse

data class SetupShopResponseModel(
        val responseData: List<ResponseData>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            val id: Int,
            @SerializedName("company_id") val companyId: Int,
            val name: String,
            val status: Int,
            @SerializedName("providerservice") val providerService: ProviderService?
    ) {
        data class ProviderService(
                val id: Int,
                @SerializedName("provider_id") val providerId: Int,
                @SerializedName("admin_service_id") val adminServiceId: Int,
                @SerializedName("provider_vehicle_id") val providerVehicleId: Any,
                @SerializedName("ride_delivery_id") val rideDeliveryId: Any,
                @SerializedName("service_id") val serviceId: Any,
                @SerializedName("category_id") val categoryId: Int,
                @SerializedName("sub_category_id") val subCategoryId: Int,
                @SerializedName("company_id") val companyId: Int,
                @SerializedName("base_fare") val baseFare: String,
                @SerializedName("per_miles") val perMiles: String,
                @SerializedName("per_mins") val perMins: String,
                val status: String,
                @SerializedName("providervehicle") val providerVehicle: ProviderVehicleResponseModel
        )
    }
}