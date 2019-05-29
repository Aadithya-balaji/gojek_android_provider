package com.gox.partner.models

data class TransportHistory(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: TransportResponseData = TransportResponseData(),
        val statusCode: String = "",
        val title: String = ""
) {
    data class TransportResponseData(
            val total_records: Int? = 0,
            val transport: List<Transport> = listOf(),
            val type: String = ""
    ) {
        data class Transport(
                val assigned_at: String = "",
                val assigned_time: String = "",
                val booking_id: String = "",
                val d_address: String = "",
                val finished_time: String = "",
                val id: Int? = 0,
                val payment: Payment = Payment(),
                val provider: Provider = Provider(),
                val provider_id: Int? = 0,
                val provider_vehicle: ProviderVehicle = ProviderVehicle(),
                val provider_vehicle_id: Int? = 0,
                val ride: Ride = Ride(),
                val ride_delivery_id: Int? = 0,
                val s_address: String = "",
                val schedule_time: String = "",
                val started_time: String = "",
                val static_map: String = "",
                val status: String = "",
                val timezone: String = "",
                val user: TransportHistoryUser = TransportHistoryUser(),
                val user_id: Int? = 0
        ) {
            data class Provider(
                    val first_name: String = "",
                    val id: Int? = 0,
                    val last_name: String = "",
                    val picture: Any? = Any(),
                    val rating: Int? = 0
            )

            data class TransportHistoryUser(
                    val first_name: String = "",
                    val id: Int? = 0,
                    val last_name: String = "",
                    val picture: String = "",
                    val rating: String = ""
            )

            data class Payment(
                    val ride_request_id: Int? = 0,
                    val total: Int? = 0
            )

            data class Ride(
                    val id: Int? = 0,
                    val vehicle_image: String = "",
                    val vehicle_name: String = ""
            )

            data class ProviderVehicle(
                    val id: Int? = 0,
                    val provider_id: Int? = 0,
                    val vehicle_make: String = "",
                    val vehicle_model: String = "",
                    val vehicle_no: String = ""
            )
        }
    }
}