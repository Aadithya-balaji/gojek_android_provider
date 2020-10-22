package com.gox.partner.models

import com.google.gson.annotations.SerializedName
import com.gox.base.models.CommonResponse
import java.io.Serializable

data class SetupDeliveryResponseModel(
        val responseData: List<ResponseData>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse, Serializable {
    data class ResponseData(
            val id: Int,
            @SerializedName("delivery_name") val deliveryName: String,
            val status: Int,
            @SerializedName("providerservice") val providerService: ProviderService?,
            @SerializedName("servicelist") val serviceList: List<ServiceList>
    ) : Serializable {
        data class ProviderService(
                val id: Int,
                @SerializedName("provider_id") val providerId: Int,
                @SerializedName("admin_service_id") val adminServiceId: Int,
                @SerializedName("provider_vehicle_id") val providerVehicleId: Int,
                @SerializedName("delivery_vehicle_id") val deliveryVehicleId: Int,
                @SerializedName("service_id") val serviceId: Any,
                @SerializedName("category_id") val categoryId: Int,
                @SerializedName("sub_category_id") val subCategoryId: Int,
                @SerializedName("company_id") val companyId: Int,
                @SerializedName("base_fare") val baseFare: String,
                @SerializedName("per_miles") val perMiles: String,
                @SerializedName("per_mins") val perMins: String,
                val status: String,
                @SerializedName("providervehicle") val providerVehicle: ProviderVehicleResponseModel
        ) : Serializable

        data class ServiceList(
                val id: Int,
                @SerializedName("company_id") val companyId: Int,
                @SerializedName("delivery_type_id") val deliveryTypeId: Int,
                @SerializedName("vehicle_type") val vehicleType: String,
                @SerializedName("vehicle_name") val vehicleName: String,
                @SerializedName("vehicle_image") val vehicleImage: String,
                @SerializedName("vehicle_marker") val vehicleMarker: String,
                val capacity: Int,
                val status: Int
        ) : Serializable {
            override fun toString(): String {
                return vehicleName
            }
        }
    }
}