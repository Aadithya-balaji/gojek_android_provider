package com.gox.partner.models

import java.io.Serializable

data class TransportHistory(
        val error: List<Any>,
        val message: String,
        val responseData: TransportResponseData,
        val statusCode: String,
        val title: String
) : Serializable

data class TransportResponseData(
        val total_records: Int,
        val transport: MutableList<Transport> = mutableListOf(),
        val service: MutableList<ServiceHistory> = mutableListOf(),
        val order: MutableList<Order> = mutableListOf(),
        val type: String
) : Serializable

data class Transport(
        val assigned_at: String,
        val assigned_time: String,
        val booking_id: String,
        val d_address: String,
        val finished_time: String,
        val id: Int,
        val payment: Any,
        val provider: Provider,
        val provider_id: Int,
        val provider_vehicle: Any,
        val provider_vehicle_id: Any,
        val ride: HistoryRide? = null,
        val ride_delivery_id: Int,
        val s_address: String,
        val schedule_time: String,
        val started_time: String,
        val static_map: String,
        val status: String,
        val timezone: String,
        val user: TransportHistoryUser,
        val user_id: Int,
        val rating: Order.Rating? = Order.Rating()

        ) : Serializable

data class Provider(
        val first_name: String,
        val id: Int,
        val last_name: String,
        val picture: String,
        val rating: Double
) : Serializable

data class TransportHistoryUser(
        val first_name: String,
        val id: Int,
        val last_name: String,
        val picture: String,
        val rating: String
) : Serializable

data class HistoryRide(
        val id: Int,
        val vehicle_image: String,
        val vehicle_name: String = ""
) : Serializable

data class ServiceHistory(
        val assigned_at: String? = "",
        val booking_id: String? = "",
        val company_id: Int? = 0,
        val id: Int? = 0,
        val payment: Payment? = null,
        val provider_id: Int? = 0,
        val s_address: String? = "",
        val service: ServiceDetail? = null,
        val service_id: Int? = 0,
        val started_at: String? = "",
        val static_map: String? = "",
        val status: String? = "",
        val user: User? = null,
        val rating: Order.Rating? = Order.Rating(),
        val user_id: Int? = 0
)

data class ServiceDetail(val id: Int? = 0,
                         val service_name: String? = "")

data class Order(
        val admin_service_id: Int? = 0,
        val company_id: Int? = 0,
        val created_at: String? = "",
        val cuisines: String? = "",
        val delivery: Delivery? = Delivery(),
        val delivery_address: String? = "",
        val id: Int? = 0,
        val pickup: Pickup? = Pickup(),
        val pickup_address: String? = "",
        val provider_id: Int? = 0,
        val rating: Rating? = Rating(),
        val static_map: String? = "",
        val status: String? = "",
        val store_id: Int? = 0,
        val store_order_invoice_id: String? = "",
        val total: String? = "",
        val user_id: Int? = 0
) {
    data class Delivery(
            val flat_no: String? = "",
            val id: Int? = 0,
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0,
            val map_address: Any? = Any(),
            val street: String? = ""
    )

    data class Pickup(
            val contact_number: String? = "",
            val id: Int? = 0,
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0,
            val picture: String? = "",
            val store_location: String? = "",
            val store_name: String? = "",
            val store_type_id: Int? = 0,
            val storetype: Storetype? = Storetype()
    ) {
        data class Storetype(
                val category: String? = "",
                val company_id: Int? = 0,
                val id: Int? = 0,
                val name: String? = "",
                val status: Int? = 0
        )
    }

    data class Rating(
            val id: Int? = 0,
            val provider_comment: String? = "",
            val provider_rating: Double? = 0.0,
            val store_rating: Double? = 0.0,
            val user_comment: String? = "",
            val user_rating: Double? = 0.0
    )
}