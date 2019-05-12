package com.xjek.provider.models

data class Test(
        val order: List<Order?>? = listOf()
) {
    data class Order(
            val admin_service_id: Int? = 0,
            val company_id: Int? = 0,
            val created_at: String? = "",
            val delivery: Delivery? = Delivery(),
            val delivery_address: String? = "",
            val id: Int? = 0,
            val pickup: Pickup? = Pickup(),
            val pickup_address: String? = "",
            val provider_id: Int? = 0,
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
    }
}