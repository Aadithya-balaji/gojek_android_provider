package com.gox.partner.models

data class HistoryDetailModel(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: ResponseData,
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class ResponseData(
            val total_records: Int? = 0,
            val transport: Transport,
            val service: Service,
            val order: Order,
            val type: String? = "") {
        data class Transport(
                val admin_id: Any? = Any(),
                val assigned_at: String? = "",
                val assigned_time: String? = "",
                val booking_id: String? = "",
                val calculator: String? = "",
                val cancel_reason: Any? = Any(),
                val cancelled_by: Any? = Any(),
                val city_id: Int? = 0,
                val company_id: Int? = 0,
                val country_id: Any? = Any(),
                val created_at: String? = "",
                val currency: String? = "",
                val d_address: String? = "",
                val d_latitude: Double? = 0.0,
                val d_longitude: Double? = 0.0,
                val destination_log: String? = "",
                val dispute: Dispute? = Dispute(),
                val distance: Double? = 0.0,
                val finished_at: String? = "",
                val finished_time: String? = "",
                val id: Int? = 0,
                val is_scheduled: String? = "",
                val is_track: String? = "",
                val otp: String? = "",
                val paid: Int? = 0,
                val payment: Payment? = Payment(),
                val payment_mode: String? = "",
                val peak_hour_id: Any? = Any(),
                val promocode_id: Double? = 0.0,
                val provider_id: Int? = 0,
                val provider_rated: Double? = 0.0,
                val provider_service_id: Int? = 0,
                val provider_vehicle_id: Int? = 0,
                val rating: Rating? = Rating(),
                val request_type: String? = "",
                val ride: Ride? = Ride(),
                val ride_delivery_id: Int? = 0,
                val ride_type_id: Any? = Any(),
                val route_key: String? = "",
                val s_address: String? = "",
                val s_latitude: Double? = 0.0,
                val s_longitude: Double? = 0.0,
                val schedule_at: Any? = Any(),
                val schedule_time: String? = "",
                val service_type: ServiceType? = ServiceType(),
                val started_at: String? = "",
                val started_time: String? = "",
                val static_map: String? = "",
                val status: String? = "",
                val surge: Double? = 0.0,
                val timezone: String? = "",
                val track_distance: Double? = 0.0,
                val track_latitude: Double? = 0.0,
                val track_longitude: Double? = 0.0,
                val travel_time: String? = "",
                val unit: String? = "",
                val use_wallet: Int? = 0,
                val user: User? = User(),
                val user_id: Int? = 0,
                val user_rated: Double? = 0.0,
                val vehicle_type: String? = ""
        ) {
            data class Ride(
                    val capacity: Int? = 0,
                    val company_id: Int? = 0,
                    val id: Int? = 0,
                    val ride_type_id: Int? = 0,
                    val status: Int? = 0,
                    val vehicle_image: String? = "",
                    val vehicle_marker: String? = "",
                    val vehicle_name: String? = "",
                    val vehicle_type: String? = ""
            )

            data class Dispute(
                    val comments: Any? = Any(),
                    val comments_by: String? = "",
                    val company_id: Int? = 0,
                    val created_at: String? = "",
                    val dispute_name: String? = "",
                    val dispute_title: Any? = Any(),
                    val dispute_type: String? = "",
                    val id: Int? = 0,
                    val is_admin: Int? = 0,
                    val provider_id: Int? = 0,
                    val refund_amount: Double? = 0.0,
                    val ride_request_id: Any? = Any(),
                    val status: String? = "",
                    val user_id: Int? = 0
            )

            data class Rating(
                    val id: Int? = 0,
                    val provider_comment: String? = "",
                    val request_id: Int? = 0,
                    val user_comment: Any? = Any()
            )

            data class User(
                    val city_id: Int? = 0,
                    val country_code: String? = "",
                    val country_id: Int? = 0,
                    val created_at: String? = "",
                    val currency_symbol: String? = "",
                    val email: String? = "",
                    val first_name: String? = "",
                    val gender: String? = "",
                    val id: Int? = 0,
                    val language: String? = "",
                    val last_name: String? = "",
                    val latitude: Any? = Any(),
                    val login_by: String? = "",
                    val longitude: Any? = Any(),
                    val mobile: String? = "",
                    val payment_mode: String? = "",
                    val picture: String? = "",
                    val rating: String? = "",
                    val state_id: Int? = 0,
                    val status: Int? = 0,
                    val user_type: String? = "",
                    val wallet_balance: Double? = 0.0
            )

            data class ServiceType(
                    val admin_service_id: Int? = 0,
                    val base_fare: Double? = 0.0,
                    val category_id: Int? = 0,
                    val company_id: Int? = 0,
                    val id: Int? = 0,
                    val per_miles: Double? = 0.0,
                    val per_mins: Double? = 0.0,
                    val provider_id: Int? = 0,
                    val provider_vehicle_id: Int? = 0,
                    val ride_delivery_id: Any? = Any(),
                    val service_id: Any? = Any(),
                    val status: String? = "",
                    val sub_category_id: Any? = Any()
            )

            data class Payment(
                    val card: Double = 0.0,
                    val cash: Double = 0.0,
                    val commision: Double = 0.0,
                    val commision_percent: Double = 0.0,
                    val company_id: Int? = 0,
                    val discount: Double = 0.0,
                    val discount_percent: Double = 0.0,
                    val distance: Double = 0.0,
                    val fixed: Double = 0.0,
                    val fleet: Int? = 0,
                    val fleet_id: Int? = 0,
                    val fleet_percent: Double = 0.0,
                    val hour: Double? = 0.0,
                    val id: Int? = 0,
                    val is_partial: Any? = Any(),
                    val minute: Double? = 0.0,
                    val payable: Double? = 0.0,
                    val payment_id: Any? = Any(),
                    val payment_mode: String? = "",
                    val peak_amount: Double? = 0.0,
                    val peak_comm_amount: Double? = 0.0,
                    val promocode_id: Double? = 0.0,
                    val provider_id: Int? = 0,
                    val provider_pay: Double? = 0.0,
                    val ride_request_id: Int? = 0,
                    val round_of: Double? = 0.0,
                    val tax: Double? = 0.0,
                    val tax_percent: Double? = 0.0,
                    val tips: Double? = 0.0,
                    val toll_charge: Double? = 0.0,
                    val total: Double? = 0.0,
                    val total_waiting_time: Int? = 0,
                    val user_id: Int? = 0,
                    val waiting_amount: Double? = 0.0,
                    val waiting_comm_amount: Double? = 0.0,
                    val wallet: Double? = 0.0
            )
        }


        data class Service(
                val assigned_at: String? = "",
                val booking_id: String? = "",
                val company_id: Int? = 0,
                val id: Int? = 0,
                val payment: Payment? = Payment(),
                val provider_id: Int? = 0,
                val s_address: String? = "",
                val service: Service? = Service(),
                val service_id: Int? = 0,
                val started_at: String? = "",
                val static_map: String? = "",
                val status: String? = "",
                val user: User? = User(),
                val dispute: Dispute? = Dispute(),
                val user_id: Int? = 0,
                val rating: Transport.Rating? = Transport.Rating()
                ) {
            data class User(
                    val first_name: String? = "",
                    val id: Int? = 0,
                    val last_name: String? = "",
                    val mobile: String? = "",
                    val picture: String? = "",
                    val rating: String? = ""
            )

            data class Dispute(
                    val comments: Any? = Any(),
                    val comments_by: String? = "",
                    val company_id: Int? = 0,
                    val created_at: String? = "",
                    val dispute_name: String? = "",
                    val dispute_title: Any? = Any(),
                    val dispute_type: String? = "",
                    val id: Int? = 0,
                    val is_admin: Int? = 0,
                    val provider_id: Int? = 0,
                    val refund_amount: Int? = 0,
                    val service_request_id: Int? = 0,
                    val status: String? = "",
                    val user_id: Int? = 0
            )

            data class Service(
                    val id: Int? = 0,
                    val service_category_id: Int? = 0,
                    val service_name: String? = ""
            )

            data class Payment(
                    val extra_charges: Double = 0.0,
                    val fixed: Double? = 0.0,
                    val id: Int? = 0,
                    val minute: Double? = 0.0,
                    val payable: Double? = 0.0,
                    val payment_mode: String? = "",
                    val service_request_id: Int? = 0,
                    val tax: Double? = 0.0,
                    val total: Double? = 0.0,
                    val discount: Double = 0.0,
                    val round_of: Double? = 0.0,
                    val tips: Double? = 0.0,
                    val wallet: Double? = 0.0,
                    val hour: Double? = 0.0,
                    val distance: Double? = 0.0
            )
        }


        data class Order(
                val admin_service_id: Int? = 0,
                val company_id: Int? = 0,
                val created_at: String? = "",
                val delivery: Delivery? = Delivery(),
                val delivery_address: String? = "",
                val id: Int? = 0,
                val order_invoice: OrderInvoice? = OrderInvoice(),
                val pickup: Pickup? = Pickup(),
                val pickup_address: String? = "",
                val provider_id: Int? = 0,
                val rating: Rating? = Rating(),
                val static_map: String? = "",
                val status: String? = "",
                val store_order_invoice_id: String? = "",
                val user: User? = User(),
                val dispute: Dispute? = Dispute(),
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

            data class Dispute(
                    val comments: Any? = Any(),
                    val comments_by: String? = "",
                    val company_id: Int? = 0,
                    val created_at: String? = "",
                    val dispute_name: String? = "",
                    val dispute_title: Any? = Any(),
                    val dispute_type: String? = "",
                    val id: Int? = 0,
                    val is_admin: Int? = 0,
                    val provider_id: Int? = 0,
                    val refund_amount: Double = 0.0,
                    val service_request_id: Int? = 0,
                    val status: String? = "",
                    val user_id: Int? = 0
            )

            data class User(
                    val first_name: String? = "",
                    val id: Int? = 0,
                    val last_name: String? = "",
                    val mobile: String? = "",
                    val picture: String? = "",
                    val rating: String? = ""
            )

            data class OrderInvoice(
                    val cart_details: String? = "",
                    val delivery_amount: Double? = 0.0,
                    val gross: Double? = 0.0,
                    val id: Int? = 0,
                    val items: List<Item?>? = listOf(),
                    val payable: Double? = 0.0,
                    val payment_mode: String? = "",
                    val promocode_amount: Double? = 0.0,
                    val discount: Double? = 0.0,
                    val store_order_id: Int? = 0,
                    val tax_amount: Double? = 0.0,
                    val total_amount: Double? = 0.0,
                    val store_package_amount: Double? = 0.0,
                    val wallet_amount: Double? = 0.0
            ) {
                data class Item(
                        val cartaddon: List<Cartaddon?>? = listOf(),
                        val company_id: Int? = 0,
                        val id: Int? = 0,
                        val item_price: Double? = 0.0,
                        val note: Any? = Any(),
                        val product: Product? = Product(),
                        val product_data: Any? = Any(),
                        val quantity: Int? = 0,
                        val store: Store? = Store(),
                        val store_id: Int? = 0,
                        val store_item_id: Int? = 0,
                        val store_order_id: Int? = 0,
                        val tot_addon_price: Double? = 0.0,
                        val total_item_price: Double? = 0.0,
                        val user_id: Int? = 0
                ) {
                    data class Product(
                            val id: Int? = 0,
                            val is_veg: String? = "",
                            val item_discount: Double? = 0.0,
                            val item_discount_type: String? = "",
                            val item_name: String? = "",
                            val item_price: Double? = 0.0,
                            val picture: String? = ""
                    )

                    data class Store(
                            val commission: Double? = 0.0,
                            val free_delivery: Int? = 0,
                            val id: Int? = 0,
                            val offer_min_amount: String? = "",
                            val offer_percent: Double = 0.0,
                            val store_cusinie: List<StoreCusinie?>? = listOf(),
                            val store_gst: Double = 0.0,
                            val store_name: String? = "",
                            val store_packing_charges: String? = "",
                            val store_type_id: Int? = 0,
                            val storetype: Storetype? = Storetype()
                    ) {
                        data class StoreCusinie(
                                val company_id: Int? = 0,
                                val cuisine: Cuisine? = Cuisine(),
                                val cuisines_id: Int? = 0,
                                val id: Int? = 0,
                                val store_id: Int? = 0,
                                val store_type_id: Int? = 0
                        ) {
                            data class Cuisine(
                                    val company_id: Int? = 0,
                                    val id: Int? = 0,
                                    val name: String? = "",
                                    val status: Int? = 0,
                                    val store_type_id: Int? = 0
                            )
                        }

                        data class Storetype(
                                val category: String? = "",
                                val company_id: Int? = 0,
                                val id: Int? = 0,
                                val name: String? = "",
                                val status: Int? = 0
                        )
                    }

                    data class Cartaddon(
                            val addon_name: String? = "",
                            val addon_price: Double? = 0.0,
                            val company_id: Int? = 0,
                            val id: Int? = 0,
                            val store_addon_id: Int? = 0,
                            val store_cart_id: Int? = 0,
                            val store_cart_item_id: Int? = 0,
                            val store_item_addons_id: Int? = 0
                    )
                }
            }

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
                    val provider_rating: Double = 0.0,
                    val store_rating: Double = 0.0,
                    val user_comment: String? = "",
                    val user_rating: Double = 0.0
            )
        }

    }
}

