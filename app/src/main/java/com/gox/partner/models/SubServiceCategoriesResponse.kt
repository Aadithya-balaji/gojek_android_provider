package com.gox.partner.models

import java.io.Serializable

data class SubServiceCategoriesResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) : Serializable {
    data class ResponseData(
            val id: String = "",
            val service_subcategory_id: String = "",
            val company_id: String = "",
            val service_subcategory_name: String = "",
            val picture: String = "",
            val providerservicesubcategory: List<ProviderServiceCategory> = listOf(),
            val service_subcategory_order: String = "",
            val service_subcategory_status: String = ""
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
