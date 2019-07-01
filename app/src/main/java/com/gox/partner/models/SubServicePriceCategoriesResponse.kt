package com.gox.partner.models

import java.io.Serializable

data class SubServicePriceCategoriesResponse(
        var error: List<Any?>? = listOf(),
        var message: String? = "",
        var responseData: MutableList<SubServicePriceResponseData?>? = mutableListOf(),
        var statusCode: String? = "",
        var title: String? = ""
) : Serializable

data class SubServicePriceResponseData(
        var allow_after_image: Int? = 0,
        var allow_before_image: Int? = 0,
        var allow_desc: Int? = 0,
        var company_id: Int? = 0,
        var id: Int? = 0,
        var is_professional: Int? = 0,
        var picture: String? = "",
        var providerservices: MutableList<ProviderService?>? = mutableListOf(),
        var service_category_id: Int? = 0,
        var service_city: ServiceCity? = ServiceCity(),
        var service_name: String? = "",
        var service_status: Int? = 0,
        var selected: String = "",
        var service_subcategory_id: Int? = 0
) : Serializable

data class ServiceCity(
        var allow_quantity: Int? = 0,
        var base_distance: Int? = 0,
        var base_fare: Int? = 0,
        var cancellation_charge: Int? = 0,
        var cancellation_time: Any? = Any(),
        var city_id: Int? = 0,
        var commission: Int? = 0,
        var company_id: Int? = 0,
        var country_id: Int? = 0,
        var fare_type: String? = "",
        var fleet_commission: Int? = 0,
        var id: Int? = 0,
        var max_quantity: Int? = 0,
        var minimum_fare: Int? = 0,
        var per_miles: Int? = 0,
        var per_mins: Int? = 0,
        var service_id: Int? = 0,
        var status: Int? = 0,
        var tax: Int? = 0
) : Serializable

data class ProviderService(
        var admin_service_id: Int? = 0,
        var base_fare: Int? = 0,
        var category_id: Int? = 0,
        var company_id: Int? = 0,
        var id: Int? = 0,
        var per_miles: Int? = 0,
        var per_mins: Int? = 0,
        var provider_id: Int? = 0,
        var provider_vehicle_id: Any? = Any(),
        var ride_delivery_id: Any? = Any(),
        var service_id: Int? = 0,
        var status: String? = "",
        var sub_category_id: Int? = 0
) : Serializable