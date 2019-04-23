package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

data class SetupRideResponseModel(
        val responseData: List<ResponseData>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            val id: Int,
            @SerializedName("ride_name") val rideName: String,
            val status: Int,
            @SerializedName("providerservice") val providerService: ProviderService,
            @SerializedName("servicelist") val serviceList: List<ServiceList>
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

        data class ServiceList(
                val id: Int,
                @SerializedName("company_id") val companyId: Int,
                @SerializedName("ride_type_id") val rideTypeId: Int,
                @SerializedName("vehicle_type") val vehicleType: String,
                @SerializedName("vehicle_name") val vehicleName: String,
                @SerializedName("vehicle_image") val vehicleImage: String,
                @SerializedName("vehicle_marker") val vehicleMarker: String,
                val capacity: Int,
                val status: Int
        )
    }
}