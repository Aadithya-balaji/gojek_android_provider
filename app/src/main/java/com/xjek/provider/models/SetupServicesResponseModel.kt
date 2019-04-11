package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

data class SetupServicesResponseModel(
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
            @SerializedName("providerservice") val providerService: List<ProviderService>
    ) {
        data class ProviderService(
                val id: Int,
                @SerializedName("provider_id") val providerId: Int,
                @SerializedName("admin_service_id") val adminServiceId: Int,
                @SerializedName("provider_vehicle_id") val providerVehicleId: Int,
                @SerializedName("ride_delivery_id") val rideDeliveryId: Any,
                @SerializedName("service_id") val serviceId: Any,
                @SerializedName("category_id") val categoryId: Int,
                @SerializedName("sub_category_id") val subCategoryId: Any,
                @SerializedName("company_id") val companyId: Int,
                @SerializedName("base_fare") val baseFare: String,
                @SerializedName("per_miles") val perMiles: String,
                @SerializedName("per_mins") val perMins: String,
                val status: String,
                @SerializedName("providervehicle") val providerVehicle: ProviderVehicle
        ) {
            data class ProviderVehicle(
                    val id: Int,
                    @SerializedName("provider_id") val providerId: Int,
                    @SerializedName("vehicle_service_id") val vehicleServiceId: Int,
                    @SerializedName("vehicle_year") val vehicleYear: Int,
                    @SerializedName("vehicle_color") val vehicleColor: String,
                    @SerializedName("vehicle_make") val vehicleMake: String,
                    @SerializedName("company_id") val companyId: Int,
                    @SerializedName("vehicle_model") val vehicleModel: String,
                    @SerializedName("vehicle_no") val vehicleNo: String,
                    @SerializedName("vechile_image") val vechileImage: String,
                    val picture: String,
                    val picture1: String
            )
        }
    }
}