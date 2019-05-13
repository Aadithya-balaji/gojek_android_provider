package com.xjek.provider.models

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
            val providerservicecategory: List<ProviderServiceCategory> = listOf(),
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
            val base_fare: String = "",
            val per_miles: String = "",
            val per_mins: String = "",
            val status: String = ""
    ) : Serializable
}
