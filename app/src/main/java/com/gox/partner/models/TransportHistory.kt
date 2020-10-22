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
        val delivery:MutableList<Deliveryview> = mutableListOf(),
        val type: String
) : Serializable

data class Transport(
        val assigned_at: String,
        val assigned_time: String,
        val booking_id: String,
        val d_address: String,
        val finished_time: String,
        val id: Int,
        val dispute_count: Int,
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
        val dispute_count: Int?=0,
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

data class Deliveryview(
        val assigned_at: String = "",
        val assigned_time: String = "",
        val booking_id: String = "",
        val created_time: String = "",
        val d_address: Any = Any(),
        val delivery_vehicle_id: Int = 0,
        val finished_time: String = "",
        val id: Int = 0,
        val payment: Payment = Payment(),
        val provider: Provider = Provider(),
        val provider_id: Int = 0,
        val provider_vehicle: Any = Any(),
        val provider_vehicle_id: Any = Any(),
        val rating: Order.Rating? = Order.Rating(),
        val s_address: String = "",
        val schedule_time: String = "",
        val service: Service = Service(),
        val service_type: ServiceType = ServiceType(),
        val started_at: String = "",
        val started_time: String = "",
        val static_map: String = "",
        val status: String = "",
        val timezone: String = "",
        val user: User = User(),
        val user_id: Int = 0
) {
    data class Payment(
            val card: Int = 0,
            val cash: Int = 0,
            val commision: Double = 0.0,
            val commision_percent: Int = 0,
            val delivery_id: Int = 0,
            val discount: Int = 0,
            val discount_percent: Int = 0,
            val distance: Double = 0.0,
            val fixed: Double = 0.0,
            val fleet: Int = 0,
            val fleet_id: Any = Any(),
            val fleet_percent: Double = 0.0,
            val id: Int = 0,
            val is_partial: Any = Any(),
            val payable: Double = 0.0,
            val payment_id: Any = Any(),
            val payment_mode: String = "",
            val peak_amount: Double = 0.0,
            val peak_comm_amount: Double = 0.0,
            val promocode_id: Any = Any(),
            val provider_id: Int = 0,
            val provider_pay: Double = 0.0,
            val round_of: Double = 0.0,
            val tax: Double = 0.0,
            val tax_percent: Double = 0.0,
            val tips: Double = 0.0,
            val total: Double = 0.0,
            val total_waiting_time: Int = 0,
            val user_id: Int = 0,
            val wallet: Double = 0.0,
            val weight: Double = 0.0
    )

    data class Provider(
            val currency_symbol: String = "",
            val current_ride_vehicle: Any = Any(),
            val current_store: Any = Any(),
            val first_name: String = "",
            val id: Int = 0,
            val last_name: String = "",
            val picture: String = "",
            val rating: Double = 0.0
    )

    data class Service(
            val id: Int = 0,
            val vehicle_image: String = "",
            val vehicle_name: String = ""
    )

    data class ServiceType(
            val admin_service: String = "",
            val base_fare: Int = 0,
            val category_id: Int = 0,
            val company_id: Int = 0,
            val delivery_vehicle_id: Any = Any(),
            val id: Int = 0,
            val per_miles: Int = 0,
            val per_mins: Int = 0,
            val provider_id: Int = 0,
            val provider_vehicle_id: Any = Any(),
            val ride_delivery_id: Any = Any(),
            val service_id: Int = 0,
            val status: String = "",
            val sub_category_id: Int = 0
    )

    data class User(
            val currency_symbol: String = "",
            val first_name: String = "",
            val id: Int = 0,
            val last_name: String = "",
            val picture: Any = Any(),
            val rating: Double = 0.0
    )
}

data class Order(
        val admin_service_id: Int? = 0,
        val company_id: Int? = 0,
        val created_at: String? = "",
        val cuisines: String? = "",
        val delivery: Delivery? = Delivery(),
        val delivery_address: String? = "",
        val id: Int? = 0,
        val dispute_count: Int? = 0,
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