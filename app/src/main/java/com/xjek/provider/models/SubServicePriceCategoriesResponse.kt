package com.xjek.provider.models

import java.io.Serializable

data class SubServicePriceCategoriesResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) : Serializable {
    data class ResponseData(
            val id: String = "",
            val service_category_id: String = "",
            val service_subcategory_id: String = "",
            val company_id: String = "",
            val service_name: String = "",
            val picture: String = "",
            val allow_desc: String = "",
            val allow_before_image: String = "",
            val allow_after_image: String = "",
            val is_professional: String = "",
            val service_status: String = "",
            var selected: String = "",
            var providerservices: MutableList<ProviderServices> = mutableListOf(),
            var servicescityprice: Servicescityprice = Servicescityprice()
    ) : Serializable

    data class ProviderServices(
            val id: String = "",
            val provider_id: String = "",
            val admin_service_id: String = "",
            val provider_vehicle_id: String = "",
            val ride_delivery_id: String = "",
            val service_id: String = "",
            val category_id: String = "",
            val sub_category_id: String = "",
            val company_id: String = "",
            var base_fare: String = "",
            var per_miles: String = "",
            var per_mins: String = "",
            val status: String = ""
    ) : Serializable

    data class Servicescityprice(
            val id: String = "",
            val service_id: String = "",
            val country_id: String = "",
            val city_id: String = "",
            val company_id: String = "",
            var fare_type: String = "",
            var base_fare: String = "",
            val base_distance: String = "",
            var per_miles: String = "",
            var per_mins: String = "",
            val minimum_fare: String = "",
            val commission: String = "",
            val fleet_commission: String = "",
            val tax: String = "",
            val cancellation_time: String = "",
            val cancellation_charge: String = "",
            val allow_quantity: String = "",
            val max_quantity: String = "",
            val status: String = ""
    ) : Serializable
}
