package com.gox.partner.models

import java.io.Serializable

data class ServiceCategoriesResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) : Serializable {
    data class ResponseData(
            val company_id: Int? = 0,
            val id: Int? = 0,
            val picture: String = "",
            val price_choose: String = "",
            val providerservicecategory: List<ProviderServiceCategory> = listOf(),
            val service_category_name: String = "",
            val service_category_order: Int? = 0,
            val service_category_status: Int? = 0
    ) : Serializable
    data class ProviderServiceCategory(
            val id: String = "",
            val provider_id: String = "",
            val admin_service_id: String = "",
            val provider_vehicle_id: String = "",
            val ride_delivery_id: String = "",
            val service_id: String = "",
            val category_id: String = "",
            val sub_category_id: String = "",
            val company_id: String = "",
            val base_fare: Double? = 0.0,
            val per_miles: Double? = 0.0,
            val per_mins: Double? = 0.0,
            val status: String = ""
    ) : Serializable
}
