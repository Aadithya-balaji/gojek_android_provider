package com.gox.partner.models

data class HistoryModel(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: TransportResponseData,
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class ResponseData(
            val total_records: Int? = 0,
            val transport: ArrayList<Transport>? = ArrayList(),
            val service: ArrayList<Service>? = ArrayList(),
            val order: ArrayList<Order>? = ArrayList(),
            val type: String? = ""
    ) {
        data class Transport(
                val assigned_at: String? = "",
                val assigned_time: String? = "",
                val booking_id: String? = "",
                val d_address: String? = "",
                val finished_time: String? = "",
                val id: Int? = 0,
                val provider: Provider? = Provider(),
                val provider_id: Int? = 0,
                val provider_vehicle: ProviderVehicle? = ProviderVehicle(),
                val provider_vehicle_id: Int? = 0,
                val ride: Ride? = Ride(),
                val ride_delivery_id: Int? = 0,
                val s_address: String? = "",
                val schedule_time: String? = "",
                val started_time: String? = "",
                val static_map: String? = "",
                val status: String? = "",
                val timezone: String? = "",
                val user: User? = User(),
                val user_id: Int? = 0
        ) {
            data class User(
                    val first_name: String? = "",
                    val id: Int? = 0,
                    val last_name: String? = "",
                    val picture: String? = "",
                    val rating: Double? = 0.0
            )

            data class Payment(
                    val ride_request_id: Int? = 0,
                    val total: Double = 0.0,
                    val card: Double = 0.0
            )

            data class ProviderVehicle(
                    val id: Int? = 0,
                    val provider_id: Int? = 0,
                    val vehicle_make: String? = "",
                    val vehicle_model: String? = "",
                    val vehicle_no: String? = ""
            )

            data class Ride(
                    val id: Int? = 0,
                    val vehicle_image: String? = "",
                    val vehicle_name: String? = ""
            )

            data class Provider(
                    val first_name: String? = "",
                    val id: Int? = 0,
                    val last_name: String? = "",
                    val picture: String? = "",
                    val rating: Double? = 0.0
            )
        }


        data class Service(
                val assigned_at: String? = "",
                val booking_id: String? = "",
                val company_id: Int? = 0,
                val id: Int? = 0,
                val provider_id: Int? = 0,
                val s_address: String? = "",
                val service: Service? = Service(),
                val service_id: Int? = 0,
                val started_at: String? = "",
                val static_map: String? = "",
                val status: String? = "",
                val user: User? = User(),
                val user_id: Int? = 0
        ) {
            data class User(
                    val first_name: String? = "",
                    val id: Int? = 0,
                    val last_name: String? = "",
                    val rating: String? = ""
            )

            data class Service(
                    val id: Int? = 0,
                    val service_category_id: Int? = 0,
                    val service_name: String? = ""
            )

            data class Payment(
                    val id: Int? = 0,
                    val service_request_id: Int? = 0,
                    val total: Double = 0.0,
                    val card: Double = 0.0
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
                    val provider_rating: Int? = 0,
                    val store_rating: Double? = 0.0,
                    val user_comment: String? = "",
                    val user_rating: Double? = 0.0
            )
        }

    }

}